package com.mintrocket.modules.auth.core_ui.view

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.text.InputFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.mintrocket.modules.auth.core_ui.R
import com.mintrocket.uicore.dpToPx
import timber.log.Timber

class EditTextCode @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val cellPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Color.BLACK
        strokeWidth = 1.dpToPx.toFloat()
        style = Paint.Style.STROKE
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 24.dpToPx.toFloat()
    }

    private val itemCenterPoint = PointF()

    private var dotDiameter = 20F
    private var dotRadius = 10F
    private var cellSpace = 20F
    private var spaceForDot = 30F
    private var dotsCount = 6

    private var filledColor: Int = Color.WHITE
    private var emptyColor: Int = Color.DKGRAY
    private var defaultStrokeColor: Int = Color.DKGRAY
    private var errorStrokeColor: Int = Color.RED

    private var cornersRadius = 18.dpToPx.toFloat()
    private val cellTop = 1.dpToPx.toFloat()
    private var cellWidth = 40.dpToPx.toFloat()

    var drawWithError = false
        set(value) {
            field = value
            invalidate()
        }

    init {
        isCursorVisible = false
        //prevent context text selection
        setOnLongClickListener { true }
        setTextIsSelectable(false)

        attrs?.let {
            initAttributes(context, it)
        }

        dotRadius = dotDiameter / 2
        spaceForDot = dotDiameter + cellSpace
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.EditTextCode)

        try {
            dotDiameter =
                array.getDimension(R.styleable.EditTextCode_dot_diameter, 32.dpToPx.toFloat())
            cellSpace = array.getDimension(R.styleable.EditTextCode_dot_space, 16.dpToPx.toFloat())
            val dotsCount = array.getInt(R.styleable.EditTextCode_dot_count, 4)
            filledColor = array.getColor(R.styleable.EditTextCode_dot_active_color, Color.WHITE)
            emptyColor = array.getColor(R.styleable.EditTextCode_dot_inactive_color, Color.DKGRAY)
            defaultStrokeColor =
                array.getColor(R.styleable.EditTextCode_cell_default_stroke_color, Color.DKGRAY)
            errorStrokeColor =
                array.getColor(R.styleable.EditTextCode_cell_error_stroke_color, Color.RED)
            cellWidth = array.getDimension(R.styleable.EditTextCode_cell_width, 40.dpToPx.toFloat())
            setDotsCount(dotsCount)
        } catch (ex: Exception) {
            Timber.e(ex)
        } finally {
            array.recycle()
        }
    }

    fun setDotsCount(count: Int) {
        dotsCount = count
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(dotsCount))
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            //drawDots(it)
            drawCells(it)
            drawText(it)
            drawCursor(it)
        }
    }

    private fun drawDots(canvas: Canvas) {
        val chainLength = (dotRadius * 2 + cellSpace) * (dotsCount - 1)
        val firstCenter = width.toFloat() / 2 - chainLength / 2

        for (i in 0 until dotsCount) {
            updateItemCenterPoint(i, firstCenter)

            if (i < text?.length ?: 0) {
                cellPaint.color = filledColor
            } else {
                cellPaint.color = emptyColor
            }

            canvas.drawCircle(itemCenterPoint.x, itemCenterPoint.y, dotRadius, cellPaint)
        }
    }

    private fun drawCells(canvas: Canvas) {
        val chainLength = cellWidth * dotsCount + cellSpace * (dotsCount - 1)

        cellPaint.color = if (drawWithError) errorStrokeColor else defaultStrokeColor

        var startX = (width.toFloat() / 2) - chainLength / 2
        for (i in 0 until dotsCount) {
            canvas.drawRoundRect(
                startX,
                cellTop,
                startX + cellWidth,
                height.toFloat() - cellTop * 2,
                cornersRadius,
                cornersRadius,
                cellPaint
            )

            startX += cellSpace + cellWidth
        }
    }

    private fun drawText(canvas: Canvas) {
        val chainLength = cellWidth * dotsCount + cellSpace * (dotsCount - 1)
        //let's assume all chars have same width for now
        val textRect = Rect()
        paint.getTextBounds("0", 0, 1, textRect)
        paint.color = currentTextColor
        //val charLength = paint.measureText("0")
        var startX =
            (width.toFloat() / 2) - chainLength / 2 + cellWidth / 2 - textRect.width().toFloat() / 2
        val startY = height.toFloat() / 2 + textRect.height().toFloat() / 2

        text?.toString()?.asIterable()?.forEachIndexed { index, c ->
            canvas.drawText(c.toString(), startX, startY, paint)
            startX += cellWidth + cellSpace
        }
    }

    private fun drawCursor(canvas: Canvas) {
        val textLength = text?.length ?: 0
        if (textLength >= dotsCount || !showCursor) return

        val chainLength = cellWidth * dotsCount + cellSpace * (dotsCount - 1)
        var startChainX = (width.toFloat() / 2) - chainLength / 2

        val cursorX = startChainX + cellWidth / 2 + cellWidth * textLength + cellSpace * textLength
        val cursorHeight = 32.dpToPx.toFloat()

        canvas.drawRect(
            cursorX,
            height.toFloat() / 2 - cursorHeight / 2,
            cursorX + 1.dpToPx.toFloat(),
            height.toFloat() / 2 + cursorHeight / 2,
            paint
        )
    }

    private var cursorHandler: Handler? = null
    private var showCursor = true

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        cursorHandler?.removeCallbacksAndMessages(null)
        cursorHandler = Handler()
        cursorHandler?.postDelayed(object : Runnable {
            override fun run() {
                showCursor = !showCursor
                invalidate()
                cursorHandler?.postDelayed(this, 500)
            }
        }, 500)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cursorHandler?.removeCallbacksAndMessages(null)
    }

    private fun updateItemCenterPoint(position: Int, firstCenter: Float) {
        val cx = (firstCenter + spaceForDot * position)
        val cy = height.toFloat() / 2
        itemCenterPoint.set(cx, cy)
    }
}