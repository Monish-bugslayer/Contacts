package com.monish.recyclermultiview

import android.content.res.TypedArray

fun TypedArray.getDimensionOrDefaultInPixelSize(attr: Int, defaultResource: Int)
        = getDimensionPixelSize(attr, resources.getDimensionPixelSize(defaultResource))