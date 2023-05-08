package com.monish.recyclermultiview

import android.graphics.Point
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.round

class VerticalBubbleScrollBarLayoutManager : BubbleScrollBarLayoutManager {

    override fun calculateThumbPosition(
        viewComponents: BubbleScrollBarViewComponents?,
        outThumbPosition: Point?
    ) {
        val recyclerView: RecyclerView = viewComponents?.recyclerView ?: return
        val verticalScrollRange: Int =
            recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent()
        val height: Int = recyclerView.height - viewComponents.thumb.height
        outThumbPosition?.set(viewComponents.thumb.x.toInt(),
            (recyclerView.computeVerticalScrollOffset() * (height / verticalScrollRange))
        )
    }

    private fun getScrollableHeight(recyclerView: RecyclerView): Int {
        return recyclerView.height - recyclerView.paddingTop - recyclerView.paddingBottom
    }

    override fun calculateBubblePosition(
        viewComponents: BubbleScrollBarViewComponents?,
        outBubblePosition: Point?
    ) {
        viewComponents?.thumb?.let { getThumbCenterY(it) }?.toInt()?.coerceAtLeast(0)?.let {
            viewComponents.bubble.x.toInt().let { it1 ->
                outBubblePosition?.set(
                    it1,
                    it
                )
            }
        }
    }

    private fun getThumbCenterY(thumb: View): Int {
        return thumb.y.toInt() - thumb.height / 2
    }

    override fun calculateScrollState(attachedRecyclerView: RecyclerView?): BubbleScrollbarState {
        return if (shouldShowScrollbar(attachedRecyclerView)) BubbleScrollbarState.HIDDEN_BUBBLE else BubbleScrollbarState.NO_SCROLLBAR
    }

    override fun shouldShowScrollbar(attachedRecyclerView: RecyclerView?): Boolean {
        return (attachedRecyclerView != null
                && attachedRecyclerView.computeVerticalScrollRange() > attachedRecyclerView.height)
    }

    override fun getScrollTarget(
        event: MotionEvent?,
        viewComponents: BubbleScrollBarViewComponents?
    ): Int {
        val recyclerView: RecyclerView = viewComponents?.recyclerView ?: return 0
        var percentage =
            (event!!.y / getScrollableHeight(viewComponents.recyclerView!!).toFloat()).toDouble()
        percentage = 0.0.coerceAtLeast(percentage.coerceAtMost(1.0))
        val absoluteScrollTarget =
            round(getTotalScrollableArea(recyclerView).toDouble() * percentage)
        return round(absoluteScrollTarget - recyclerView.computeVerticalScrollOffset()).toInt()
    }

    private fun getTotalScrollableArea(recyclerView: RecyclerView): Int {
        return recyclerView.computeVerticalScrollRange() - recyclerView.computeVerticalScrollExtent()
    }

    private fun getScrollProgress(viewComponents: BubbleScrollBarViewComponents): Any {
        val recyclerView: RecyclerView = viewComponents.recyclerView ?: return 0
        val scrolledPosition: Float =
            viewComponents.thumb.y + viewComponents.thumb.height / 2
        val percentage =
            scrolledPosition / viewComponents.recyclerView.let { getScrollableHeight(it!!).toFloat() }
        return 0f.coerceAtLeast(percentage.coerceAtMost(1f)).toDouble()
    }

    override fun getScrolledItemPosition(viewComponents: BubbleScrollBarViewComponents?): Int {
        val recyclerView: RecyclerView = viewComponents?.recyclerView?: return 0
        val itemView = viewComponents.thumb.let { findScrolledView(recyclerView, it) }
        if(itemView!=null){
            return recyclerView.getChildAdapterPosition(itemView)
        }
        return 0

    }

    private fun findScrolledView(recyclerView: RecyclerView, thumb: View): View? {
        var foundView: View? = null
        if (thumb.height == 0) {
            return null
        }
        var scrolledPosition = thumb.y + thumb.height / 2
        val searchStepSize = (thumb.height / STEP_RATIO).toFloat()
        var step = 0
        while (scrolledPosition < recyclerView.height
            && foundView == null
        ) {
            scrolledPosition += step * searchStepSize
            foundView = recyclerView.findChildViewUnder(recyclerView.x, scrolledPosition)
            step++
        }
        return foundView
    }

    companion object {
        const val STEP_RATIO = 10
    }
}