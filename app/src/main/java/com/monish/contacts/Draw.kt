package com.monish.contacts


import android.graphics.*
import android.widget.ImageView
import com.monish.contacts.domain.Contact
import java.util.*


object Draw {
    private val textPaint = Paint(Paint.LINEAR_TEXT_FLAG or Paint.ANTI_ALIAS_FLAG)
    private lateinit var canvas: Canvas
    private var textBounds:Rect= Rect()
    fun drawText(contact: String,profile:ImageView):Bitmap{
        val initials =
            contact.split(" ").mapNotNull { it.firstOrNull()?.toString() }
                .joinToString("").uppercase(Locale.getDefault())
        val bitmap = Bitmap.createBitmap(
            profile.width + 550, profile.height + 550, Bitmap.Config.ARGB_8888
        )

        canvas = Canvas(bitmap)
        textPaint.apply {
            isAntiAlias=true
            color = Color.WHITE
            textPaint.isFakeBoldText = true;
            textSize = (canvas.width / 3).toFloat()
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }
        val textBounds = Rect()
        textPaint.getTextBounds(initials, 0, initials.length, textBounds)
        canvas.drawText(
            initials,
            canvas.width.toFloat() / 2,
            ((canvas.height + textBounds.height()) / 2).toFloat(),
            textPaint
        )
        return bitmap
    }
    val drawTick=
        {
            textPaint.color = Color.GREEN
            textPaint.textSize = 20f // Increase the text size for the tick mark
            canvas.drawText("✔",
                canvas.width.toFloat() / 2,
                ((canvas.height + textBounds.height()) / 2).toFloat(),
                textPaint)
        }


    fun drawText(contact: String,profile:ImageView,sizeOfText:Int):Bitmap {
        val initials =
            contact.split(" ").mapNotNull { it.firstOrNull()?.toString() }
                .joinToString("").uppercase(Locale.getDefault())
        val bitmap = Bitmap.createBitmap(
            profile.width+550 , profile.height +550, Bitmap.Config.ARGB_8888
        )

        canvas = Canvas(bitmap)
        textPaint.apply {
            isAntiAlias = true
            color = Color.WHITE
            textPaint.isFakeBoldText = true;
            textSize = (canvas.width / sizeOfText).toFloat()
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
        }
        val textBounds = Rect()
        textPaint.getTextBounds(initials, 0, initials.length, textBounds)
        canvas.drawText(
            initials,
            canvas.width.toFloat() / 2,
            ((canvas.height + textBounds.height()) / 2).toFloat(),
            textPaint
        )
        return bitmap
    }
}