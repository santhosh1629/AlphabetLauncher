package com.example.alphabetlauncher.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.exp

class AlphabetBarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val density = resources.displayMetrics.density

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = 14f * density
        textAlign = Paint.Align.CENTER
        typeface = android.graphics.Typeface.DEFAULT_BOLD
    }

    private val starPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = 22f * density
        textAlign = Paint.Align.CENTER
    }

    private val bubblePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.rgb(35, 35, 35)
        style = Paint.Style.FILL
    }

    private val bubbleTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = android.graphics.Color.WHITE
        textSize = 34f * density
        textAlign = Paint.Align.CENTER
        typeface = android.graphics.Typeface.DEFAULT_BOLD
    }

    private val letters = ('A'..'Z').toList()

    private var touchY = -1f
    private var selectedIndex = -1

    var onLetterSelected: ((Char) -> Unit)? = null
    var onTouchReleased: (() -> Unit)? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val rightX = width - 24f * density

        val top = 45f * density
        val bottom = height - 30f * density

        val itemHeight = (bottom - top) / letters.size

        val maxBend = width * 0.50f
        val curveWidth = itemHeight * 5f

        canvas.drawText(
            "★",
            rightX,
            25f * density,
            starPaint
        )

        var selectedX = rightX
        var selectedY = 0f

        letters.forEachIndexed { index, letter ->

            val y = top +
                    index * itemHeight +
                    itemHeight / 2f -
                    (paint.ascent() + paint.descent()) / 2f

            val distance = if (touchY >= 0f) {
                y - touchY
            } else {
                0f
            }

            val bend = if (touchY >= 0f) {
                maxBend * exp(
                    -(distance * distance) /
                            (2f * curveWidth * curveWidth)
                )
            } else {
                0f
            }

            val x = rightX - bend

            canvas.drawText(
                letter.toString(),
                x,
                y,
                paint
            )

            if (index == selectedIndex) {
                selectedX = x
                selectedY = y
            }
        }

        if (selectedIndex >= 0) {

            canvas.drawCircle(
                selectedX,
                selectedY,
                30f * density,
                bubblePaint
            )

            canvas.drawText(
                letters[selectedIndex].toString(),
                selectedX,
                selectedY -
                        (bubbleTextPaint.ascent() +
                                bubbleTextPaint.descent()) / 2f,
                bubbleTextPaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val top = 45f * density
        val bottom = height - 30f * density

        val itemHeight = (bottom - top) / letters.size

        when (event.action) {

            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {

                touchY = event.y

                selectedIndex = ((event.y - top) / itemHeight)
                    .toInt()
                    .coerceIn(0, letters.lastIndex)

                onLetterSelected?.invoke(
                    letters[selectedIndex]
                )

                invalidate()

                return true
            }

            MotionEvent.ACTION_UP -> {

                touchY = -1f
                selectedIndex = -1

                onTouchReleased?.invoke()

                invalidate()

                performClick()

                return true
            }
        }

        return true
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}