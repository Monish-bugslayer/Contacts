package com.monish.recyclermultiview

import android.graphics.Point
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

interface BubbleScrollBarLayoutManager {
    fun calculateThumbPosition(
        viewComponents: BubbleScrollBarViewComponents?,
        outThumbPosition: Point?
    )

    fun calculateBubblePosition(
        viewComponents: BubbleScrollBarViewComponents?,
        outBubblePosition: Point?
    )

    fun calculateScrollState(attachedRecyclerView: RecyclerView?): BubbleScrollbarState?
    fun shouldShowScrollbar(attachedRecyclerView: RecyclerView?): Boolean
    fun getScrollTarget(event: MotionEvent?, viewComponents: BubbleScrollBarViewComponents?): Int
    fun getScrolledItemPosition(viewComponents: BubbleScrollBarViewComponents?): Int
}