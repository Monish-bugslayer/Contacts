package com.monish.recyclermultiview
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable

import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.monish.contacts.R


class BubbleScrollBar : FrameLayout {
    private val thumbPosition = Point()
    private val bubblePosition = Point()
    private val thumbRect = Rect()
    private val scrollBarRect = Rect()

    private var currentScrollbarState = BubbleScrollbarState.HIDDEN_BUBBLE

    private var bubbleScrollBarAnimationManager: BubbleScrollBarAnimationManager =
        VerticalBubbleScrollBarAnimationManager()
    private var showBubbleAnimation: ValueAnimator? = null
    private var hideBubbleAnimation: ValueAnimator? = null
    private var showBarAnimation: ValueAnimator? = null
    private var hideBarAnimation: ValueAnimator? = null

    private var barLayoutManager: BubbleScrollBarLayoutManager = VerticalBubbleScrollBarLayoutManager()
    private lateinit var bubbleScrollBarViewComponents: BubbleScrollBarViewComponents

    var bubbleTextProvider: BubbleTextProvider? = null

    private val onScrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            onMove()
        }
    }

    private var bubbleText: String?
        get() {
            val targetPosition = barLayoutManager.getScrolledItemPosition(bubbleScrollBarViewComponents)
            return if (targetPosition != RecyclerView.NO_POSITION)
                bubbleTextProvider?.provideBubbleText(targetPosition)
            else
                ""
        }
        set(bubbleText) {
            val bubble = bubbleScrollBarViewComponents.bubble
            bubble.text = bubbleText
            onBubbleTextChange(bubbleText)
        }

    private fun onBubbleTextChange(newText: String?) {
        bubbleScrollBarViewComponents.bubble.visibility = if (newText?.isNotEmpty() == true) View.VISIBLE else View.GONE
    }

    constructor(context: Context) : super(context) {
        initializeView()
        obtainStyledAttributes()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initializeView()
        obtainStyledAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initializeView()
        obtainStyledAttributes(attrs, defStyleAttr)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initializeView()
        obtainStyledAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private fun onMove() {
        barLayoutManager.calculateThumbPosition(bubbleScrollBarViewComponents, thumbPosition)
        moveThumb()
        barLayoutManager.calculateBubblePosition(bubbleScrollBarViewComponents, bubblePosition)
        moveBubble()
        bubbleText = bubbleText
    }

    private fun initializeView() {
        val root = LayoutInflater.from(context).inflate(R.layout.view_bubble_scroll_bar, this, true)
        bubbleScrollBarViewComponents = BubbleScrollBarViewComponents(
            root.findViewById<View>(R.id.thumb) as ImageView,
            root.findViewById(R.id.scrollBar),
            root.findViewById<View>(R.id.bubble) as TextView
        )
        initializeAnimations()
        post { barLayoutManager.calculateScrollState(bubbleScrollBarViewComponents.recyclerView)
            ?.let { setScrollState(it) } }
        post { this@BubbleScrollBar.setInitialBubblePosition() }
    }

    private var bubbleElevation = 0f
    private var bubbleMargin: Int = 0

    private var bubblePadding = 0
    private var bubbleTextSize = 0f

    private var bubbleTextColor: Int = 0

    private var bubbleMinWidth = 0
    private var bubbleHeight = 0

    private var scrollBarBackground: Drawable? = null
    private var scrollBarWidth: Int = 0

    private var bubbleBackground: Drawable? = null

    private var thumbBackground: Drawable? = null

    private fun obtainStyledAttributes(attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) {
        with(context.theme.obtainStyledAttributes(attrs, R.styleable.BubbleScrollBar, defStyleAttr, defStyleRes)) {
            scrollBarBackground = getDrawable(R.styleable.BubbleScrollBar_scrollbarBackground)
            scrollBarWidth = getDimension(
                R.styleable.BubbleScrollBar_scrollbarWidth,
                R.dimen.default_scrollbar_width.toFloat()
            ).toInt()
            thumbBackground = getDrawable(R.styleable.BubbleScrollBar_thumbBackground)
            bubbleBackground = getDrawable(R.styleable.BubbleScrollBar_bubbleBackground)
            bubbleElevation = getDimension(
                R.styleable.BubbleScrollBar_bubbleElevation,
                resources.getDimension(R.dimen.default_bubble_elevation)
            )
            bubbleTextSize = getDimension(
                R.styleable.BubbleScrollBar_bubbleTextSize,
                resources.getDimension(R.dimen.default_bubble_text_size)
            )
            bubbleTextColor = getColor(
                R.styleable.BubbleScrollBar_bubbleTextColor,
                ContextCompat.getColor(context, R.color.white)
            )
            bubbleMargin = getDimension(
                R.styleable.BubbleScrollBar_bubbleMargin,
                R.dimen.default_bubble_margin.toFloat()
            ).toInt()
            bubblePadding = getDimension(
                R.styleable.BubbleScrollBar_bubblePadding,
                R.dimen.default_bubble_padding.toFloat()
            ).toInt()
            bubbleMinWidth = getDimension(
                R.styleable.BubbleScrollBar_bubbleMinWidth,
                R.dimen.default_bubble_min_width.toFloat()
            ).toInt()
            bubbleHeight = getDimension(
                R.styleable.BubbleScrollBar_bubbleHeight,
                R.dimen.default_bubble_height.toFloat()
            ).toInt()
            return@with
        }

        with(bubbleScrollBarViewComponents) {
            scrollBar.background = scrollBarBackground
            thumb.layoutParams.width = scrollBarWidth

            thumb.layoutParams.height = bubbleHeight
            thumb.setImageDrawable(thumbBackground)

            ((bubble.layoutParams) as? MarginLayoutParams)?.marginEnd = bubbleMargin
            bubble.minWidth = bubbleMinWidth
            bubble.setTextColor(bubbleTextColor)
            bubble.textSize = bubbleTextSize
            bubble.setPadding(bubblePadding, bubblePadding, bubblePadding, bubblePadding)
            bubble.layoutParams.height = bubbleHeight
            bubble.background = bubbleBackground
            ViewCompat.setElevation(bubble, bubbleElevation)
        }
    }

    private fun setInitialBubblePosition() {
        barLayoutManager.calculateBubblePosition(bubbleScrollBarViewComponents, bubblePosition)
        moveBubble()
    }

    private fun initializeAnimations() {
        showBubbleAnimation = bubbleScrollBarAnimationManager.provideShowBubbleAnimation(bubbleScrollBarViewComponents)
        hideBubbleAnimation = bubbleScrollBarAnimationManager.provideHideBubbleAnimation(bubbleScrollBarViewComponents)
        showBarAnimation=bubbleScrollBarAnimationManager.provideShowScrollBarAnimation(bubbleScrollBarViewComponents)
        hideBarAnimation=bubbleScrollBarAnimationManager.provideHideScrollBarAnimation(bubbleScrollBarViewComponents)

        val showBubbleUpdateListener =
            bubbleScrollBarAnimationManager.provideShowBubbleUpdateListener(bubbleScrollBarViewComponents)
        val hideBubbleUpdateListener =
            bubbleScrollBarAnimationManager.provideHideBubbleUpdateListener(bubbleScrollBarViewComponents)
        val hideBarUpdateListner=bubbleScrollBarAnimationManager.provideHideScrollBarUpdateListner(bubbleScrollBarViewComponents)
        val showBarUpdateListner=bubbleScrollBarAnimationManager.provideShowScrollBarUpdateListner(bubbleScrollBarViewComponents)

        showBubbleAnimation?.addUpdateListener(showBubbleUpdateListener)
        hideBubbleAnimation?.addUpdateListener(hideBubbleUpdateListener)
    }

    private fun setScrollState(scrollStateBubble: BubbleScrollbarState) {
        this.currentScrollbarState = scrollStateBubble
        renderScrollState()
    }

    private fun renderScrollState() {
        when (currentScrollbarState) {
            BubbleScrollbarState.NO_SCROLLBAR -> onNoScroll()
            BubbleScrollbarState.VISIBLE_BUBBLE -> onVisibleBubble()
            BubbleScrollbarState.HIDDEN_BUBBLE -> onHiddenBubble()
        }
    }

    private fun onHiddenBubble() {
        playHideBubbleAnimation()
    }

    private fun onVisibleBubble() {
        playShowBubbleAnimation()
    }

    private fun onNoScroll() {
        visibility = View.GONE
    }

    private fun moveBubble() {
        val bubble = bubbleScrollBarViewComponents.bubble
        bubble.x = bubblePosition.x.toFloat()
        bubble.y = bubblePosition.y.toFloat()

    }

    private fun moveThumb() {
        val thumb = bubbleScrollBarViewComponents.thumb
        thumb.x = thumbPosition.x.toFloat()
        thumb.y = thumbPosition.y.toFloat()
    }

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        val attachedRecyclerView = bubbleScrollBarViewComponents.recyclerView
        if (attachedRecyclerView != null) {
            destroyCallbacks()
        }
        bubbleScrollBarViewComponents.recyclerView = recyclerView
        setupCallbacks()
        post { barLayoutManager.calculateScrollState(recyclerView)?.let { setScrollState(it) } }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        var handled = false
        if (shouldStartFastScrolling(event)) {
            handled = true
            setScrollState(BubbleScrollbarState.VISIBLE_BUBBLE)
        } else if (shouldContinueFastScrolling(event)) {
            bubbleScrollBarViewComponents.recyclerView?.scrollBy(0, getScrollTarget(event))
            handled = true
        } else if (shouldEndFastScrolling(event)) {
            handled = true
            setScrollState(BubbleScrollbarState.HIDDEN_BUBBLE)
        }
        return handled
    }

    private fun shouldContinueFastScrolling(event: MotionEvent): Boolean {
        return event.action == MotionEvent.ACTION_MOVE && isEventInScrollBarPosition(event)
    }

    private fun shouldStartFastScrolling(event: MotionEvent): Boolean {
        return event.action == MotionEvent.ACTION_DOWN && isEventInThumbPosition(event)
    }

    private fun shouldEndFastScrolling(event: MotionEvent): Boolean {
        return (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) && currentScrollbarState == BubbleScrollbarState.VISIBLE_BUBBLE
    }

    private fun isEventInScrollBarPosition(event: MotionEvent): Boolean =
        event.isInViewRect(bubbleScrollBarViewComponents.scrollBar, TOUCHABLE_AREA_PADDING, scrollBarRect)

    private fun isEventInThumbPosition(event: MotionEvent): Boolean =
        event.isInViewRect(bubbleScrollBarViewComponents.thumb, TOUCHABLE_AREA_PADDING, thumbRect)

    private fun playHideBubbleAnimation() {
        showBubbleAnimation?.cancel()
        if (bubbleScrollBarAnimationManager.isBubbleHidden(bubbleScrollBarViewComponents) || hideBubbleAnimation?.isRunning == true) {
            return
        }
        hideBubbleAnimation = bubbleScrollBarAnimationManager.provideHideBubbleAnimation(bubbleScrollBarViewComponents)
        hideBubbleAnimation?.addUpdateListener(
            bubbleScrollBarAnimationManager.provideHideBubbleUpdateListener(
                bubbleScrollBarViewComponents
            )
        )
        hideBubbleAnimation?.start()
    }

    private fun playShowBubbleAnimation() {
        hideBubbleAnimation?.cancel()
        if (bubbleScrollBarAnimationManager.isBubbleVisible(bubbleScrollBarViewComponents) || showBubbleAnimation?.isRunning == true) {
            return
        }
        showBubbleAnimation = bubbleScrollBarAnimationManager.provideShowBubbleAnimation(bubbleScrollBarViewComponents)
        showBubbleAnimation?.addUpdateListener(
            bubbleScrollBarAnimationManager.provideShowBubbleUpdateListener(
                bubbleScrollBarViewComponents
            )
        )
        showBubbleAnimation?.start()
    }

    private fun setupCallbacks() = bubbleScrollBarViewComponents.recyclerView?.addOnScrollListener(onScrollListener)

    private fun destroyCallbacks() =
        bubbleScrollBarViewComponents.recyclerView?.removeOnScrollListener(onScrollListener)

    private fun getScrollTarget(event: MotionEvent): Int =
        barLayoutManager.getScrollTarget(event, bubbleScrollBarViewComponents)

    fun setBubbleAnimationManager(bubbleScrollBarAnimationManager: BubbleScrollBarAnimationManager) {
        this.bubbleScrollBarAnimationManager = bubbleScrollBarAnimationManager
    }

    fun setLayoutManager(barLayoutManager: BubbleScrollBarLayoutManager) {
        this.barLayoutManager = barLayoutManager
    }

    companion object {
        private val TOUCHABLE_AREA_PADDING = dpToPx(20)
    }
}