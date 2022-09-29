package com.mintrocket.uicore.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.text.InputFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.mintrocket.uicore.R
import com.mintrocket.uicore.dpToPx
import timber.log.Timber

class EditTextCodeV2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {
        private const val CELL_SPACE = 9F
        private const val CELLS_COUNT = 8
        private const val PADDING_4 = 4
        private const val PADDING_2 = 2
        private const val DELAY = 500L
    }

    private val cellPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Color.WHITE
    }

    private val cursorPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        color = Color.WHITE
    }

    private var cellSpace = CELL_SPACE
    private var cellsCount = CELLS_COUNT

    private var defaultColor: Int = Color.YELLOW
    private var activeColor: Int = Color.WHITE
    private var errorColor: Int = Color.RED

    private val cellTop = 1.dpToPx.toFloat()
    private var cellWidth = 40.dpToPx.toFloat()
    private var autoWidth = false

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

        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(cellsCount))
    }

    private fun initAttributes(context: Context, attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.EditTextCodeV2)

        try {
            cellSpace =
                array.getDimension(R.styleable.EditTextCodeV2_cell_space, 10.dpToPx.toFloat())
            cellsCount = array.getInt(R.styleable.EditTextCodeV2_cell_count, CELLS_COUNT)
            defaultColor =
                array.getColor(R.styleable.EditTextCodeV2_cell_default_stroke_color, Color.WHITE)
            activeColor =
                array.getColor(R.styleable.EditTextCodeV2_cell_default_stroke_color, Color.YELLOW)
            errorColor =
                array.getColor(R.styleable.EditTextCodeV2_cell_error_color, Color.RED)
            cellWidth =
                array.getDimension(R.styleable.EditTextCodeV2_cell_width, 33.dpToPx.toFloat())
            autoWidth = array.getBoolean(R.styleable.EditTextCodeV2_auto_width, false)
        } catch (ex: Exception) {
            Timber.d(ex)
        } finally {
            array.recycle()
        }
    }

    fun setCellsCount(cells: Int) {
        cellsCount = cells
        filters = arrayOf<InputFilter>(InputFilter.LengthFilter(cellsCount))
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        if (autoWidth)
            cellWidth = (width - (cellsCount - 1) * cellSpace) / cellsCount
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            drawCells(it)
            drawText(it)
            drawCursor(it)
        }
    }

    private fun drawCells(canvas: Canvas) {
        val chainLength = cellWidth * cellsCount + cellSpace * (cellsCount - 1)
        val textLength = text?.length ?: 0

        var startX = (width.toFloat() / 2) - chainLength / 2
        for (i in 0 until cellsCount) {
            cellPaint.color = when {
                drawWithError -> errorColor
                i == textLength -> activeColor
                else -> defaultColor
            }
            canvas.drawRect(
                startX,
                height.toFloat() - cellTop * PADDING_4,
                startX + cellWidth,
                height.toFloat() - cellTop * PADDING_2,
                cellPaint
            )

            startX += cellSpace + cellWidth
        }
    }

    private fun drawText(canvas: Canvas) {
        val chainLength = cellWidth * cellsCount + cellSpace * (cellsCount - 1)
        //let's assume all chars have same width for now
        val textRect = Rect()
        paint.getTextBounds("0", 0, 1, textRect)
        paint.color = textColors.defaultColor
        //val charLength = paint.measureText("0")
        var startX =
            (width.toFloat() / 2) - chainLength / 2 + cellWidth / 2 - textRect.width().toFloat() / 2
        val cursorHeight = 28.dpToPx.toFloat()
        val startY = cursorHeight - (cursorHeight - textRect.height().toFloat()) / 2

        text?.toString()?.asIterable()?.forEachIndexed { index, c ->
            canvas.drawText(c.toString(), startX, startY, paint)
            startX += cellWidth + cellSpace
        }
    }

    private fun drawCursor(canvas: Canvas) {
        val textLength = text?.length ?: 0
        if (textLength >= cellsCount || !showCursor) return

        val chainLength = cellWidth * cellsCount + cellSpace * (cellsCount - 1)
        val startChainX = (width.toFloat() / 2) - chainLength / 2

        val cursorX = startChainX + cellWidth / 2 + cellWidth * textLength + cellSpace * textLength
        val cursorHeight = 28.dpToPx.toFloat()
        cursorPaint.color = activeColor
        canvas.drawRect(
            cursorX,
            0f,
            cursorX + 2.dpToPx.toFloat(),
            cursorHeight,
            cursorPaint
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
                cursorHandler?.postDelayed(this, DELAY)
            }
        }, DELAY)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cursorHandler?.removeCallbacksAndMessages(null)
    }
}