package com.monish.recyclermultiview

import android.graphics.Rect

fun Rect.addPadding(paddingDp: Int) {
    left -= paddingDp
    top -= paddingDp
    right += paddingDp
    bottom += paddingDp
}