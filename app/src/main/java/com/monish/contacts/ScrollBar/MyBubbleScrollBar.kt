package com.monish.recyclermultiview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.monish.contacts.R
import kotlin.math.max
import kotlin.math.min

class MyBubbleScrollBar(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var bubblePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        textSize = resources.getDimension(R.dimen.bubble_scroll_bar_text_size)
        textAlign = Paint.Align.CENTER
    }

    private var bubbleRect = Rect()
    private var bubbleText = ""

    private var fastScrollBarWidth = resources.getDimension(R.dimen.fast_scroll_bar_width)
    private var fastScrollBarMargin = resources.getDimension(R.dimen.fast_scroll_bar_margin)
    private var fastScrollBarPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
    }
    private var fastScrollBarRect = Rect()

    private val alphabet = listOf(
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
        "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    )

    private var onLetterSelectedListener: ((String) -> Unit)? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        val bubbleWidth = width.toFloat() - (fastScrollBarWidth + fastScrollBarMargin)
        val bubbleHeight = height.toFloat() / alphabet.size
        for (i in alphabet.indices) {
            bubbleText = alphabet[i]
            bubbleRect.set(
                0, (i * bubbleHeight).toInt(),
                bubbleWidth.toInt(), ((i + 1) * bubbleHeight).toInt()
            )
            bubblePaint.getTextBounds(bubbleText, 0, bubbleText.length, bubbleRect)
            canvas.drawText(
                bubbleText, bubbleWidth / 2,
                (bubbleRect.centerY() + bubbleRect.height() / 2).toFloat(),
                bubblePaint
            )
        }

        fastScrollBarRect.set(
            (bubbleWidth + fastScrollBarMargin).toInt(), 0, width, height
        )
        canvas.drawRect(fastScrollBarRect, fastScrollBarPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        val action = event.action
        val x = event.x
        val y = event.y

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (x < width - fastScrollBarWidth) {
                    scrollToLetter(y)
                    return true
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (x < width - fastScrollBarWidth) {
                    scrollToLetter(y)
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun scrollToLetter(y: Float) {
        val alphabetIndex = min(max((y / height * alphabet.size).toInt(), 0), alphabet.size - 1)
        bubbleText = alphabet[alphabetIndex]
        onLetterSelectedListener?.invoke(bubbleText)
    }

    fun setOnLetterSelectedListener(listener: ((String) -> Unit)?) {
        onLetterSelectedListener = listener
    }

}
