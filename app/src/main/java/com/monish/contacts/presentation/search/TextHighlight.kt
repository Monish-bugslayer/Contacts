package com.monish.contacts.presentation.search

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.TextAppearanceSpan



object TextHighlight {
    private lateinit var builder: SpannableString
    fun highlight(queryText:String,highlightText: String):SpannableString{
        if (queryText != "" || queryText.isEmpty()) {
            val startPos = highlightText.lowercase().indexOf(queryText.lowercase())
            val endPos = startPos + queryText.length
            if (startPos != -1) {
                builder = SpannableString(highlightText)
                val colorStateList = ColorStateList(
                    arrayOf(intArrayOf()), intArrayOf(
                        Color.BLUE
                    )
                )
                val textAppearanceSpan =
                    TextAppearanceSpan(null, Typeface.BOLD, -1, colorStateList, null)
                builder.setSpan(
                    textAppearanceSpan,
                    startPos,
                    endPos,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        return builder
    }
}