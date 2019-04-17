package com.theapache64.leericos

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CustomTextView(context: Context?, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F8F8F8")
        style = Paint.Style.FILL
    }

    var radius = 0f
    override fun onDraw(c: Canvas?) {
        if (text == "...") {
            c?.let { canvas ->
                val cx = width / 2.toFloat()
                val cy = height / 2.toFloat()
                val maxRad = cx
                if (radius > cx) {
                    radius = 0f
                } else {
                    radius += 10
                }

                canvas.drawCircle(cx, cy, radius, circlePaint)
            }
        }
        super.onDraw(c)
    }
}