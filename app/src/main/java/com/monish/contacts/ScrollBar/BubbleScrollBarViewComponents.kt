package com.monish.recyclermultiview

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BubbleScrollBarViewComponents(
    val thumb: ImageView,
    val scrollBar: View,
    val bubble: TextView
) {

    var recyclerView: RecyclerView? = null

}
