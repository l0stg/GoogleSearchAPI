package com.mintrocket.modules.security.core_ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import com.mintrocket.modules.security.core_ui.R
import timber.log.Timber

class PinDotsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private var filledCount = 0

    private var pinSize = 0
    private var pinGap = 0

    @DrawableRes
    private var pinBg: Int = 0

    init {
        val attrsArray =
            context.obtainStyledAttributes(attrs, R.styleable.PinDotsView, 0, R.style.PinDotStyle)
        try {
            pinSize = attrsArray.getDimensionPixelSize(R.styleable.PinDotsView_pinSize, 0)
            pinGap = attrsArray.getDimensionPixelSize(R.styleable.PinDotsView_pinSize, 0)
            pinBg = attrsArray.getResourceId(R.styleable.PinDotsView_pinBackground, 0)
        } catch (ex: Exception) {
            Timber.e(ex)
        } finally {
            attrsArray.recycle()
        }
        orientation = HORIZONTAL
    }

    fun setDotsCount(count: Int) {
        removeAllViews()
        (0 until count).forEach { index ->
            val view = createDotView(index)
            addView(view)
        }
        setFilledDots(filledCount)
    }

    fun setFilledDots(count: Int) {
        filledCount = count
        (0 until childCount).forEach { index ->
            val view = getChildAt(index)
            view.isSelected = (index + 1) <= filledCount
        }
    }

    private fun createDotView(index: Int): View = View(context, null).apply {
        layoutParams = generateDefaultLayoutParams().apply {
            width = pinSize
            height = pinSize
            if (index != 0) {
                marginStart = pinGap
            }
        }
        setBackgroundResource(pinBg)
    }
}