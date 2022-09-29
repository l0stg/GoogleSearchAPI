package com.mintrocket.uicore.recycler

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import com.mintrocket.uicore.dpToPx
import kotlin.math.roundToInt

class UniversalDecorator : RecyclerView.ItemDecoration() {

    companion object {
        const val ANY_LAYOUT = -2
        private const val SHIFT_MULTIPLIER = 31
    }

    private val dividedPairs = mutableMapOf<Int, Int>()
    private var gapsBeforeFirst = mutableMapOf<Int, Int>()
    private val specialOffsets = mutableMapOf<Int, OffsetInfo>()

    private val overridePairs = mutableMapOf<Int, Int>()
    private val overrideOffsets = mutableMapOf<Int, Pair<OffsetInfo, OffsetInfo>>()

    private var gapSize = 0

    private var viewTypes: Set<Int>? = null
    private var viewTypesDefaultDivider: Set<Int>? = null

    private var defaultDivider: Drawable? = null
    private var colorDivider: Drawable? = null

    private var defaultOffsetInfo = OffsetInfo(0, 0, 0, 0)

    fun overrideOffsetsBetween(
        firstId: Int,
        secondId: Int,
        firstOffset: OffsetInfo,
        secondOffset: OffsetInfo
    ): UniversalDecorator {
        overridePairs[firstId] = secondId
        val key = getOverrideKey(firstId, secondId)
        overrideOffsets[key] = Pair(firstOffset.pxToDp(), secondOffset.pxToDp())
        return this
    }

    fun withExtraOffsetBetween(
        gapDp: Int,
        vararg pairs: Pair<Int, Int>
    ): UniversalDecorator {
        return withExtraColorOffsetBetween(gapDp, null, *pairs)
    }

    fun withExtraColorOffsetBetween(
        gapDp: Int,
        @ColorInt color: Int?,
        vararg pairs: Pair<Int, Int>
    ): UniversalDecorator {
        gapSize = gapDp.dpToPx
        pairs.forEach {
            dividedPairs[it.first] = it.second
        }
        if (color != null) {
            colorDivider = ColorDrawable(color)
        }
        return this
    }

    fun withViewTypes(vararg layouts: Int): UniversalDecorator {
        viewTypes = layouts.toHashSet()
        return this
    }

    fun withExtraOffsetIfFirst(@IdRes id: Int, gapDp: Int): UniversalDecorator {
        gapsBeforeFirst[id] = gapDp.dpToPx
        return this
    }

    fun withOffsetFor(
        @IdRes id: Int,
        left: Int,
        right: Int,
        top: Int,
        bottom: Int
    ): UniversalDecorator {
        specialOffsets[id] = OffsetInfo(left.dpToPx, right.dpToPx, top.dpToPx, bottom.dpToPx)
        return this
    }

    fun withOffsetForTypes(
        left: Int,
        right: Int,
        top: Int,
        bottom: Int,
        @IdRes vararg ids: Int
    ): UniversalDecorator {
        val offsetInfo = OffsetInfo(left.dpToPx, right.dpToPx, top.dpToPx, bottom.dpToPx)
        ids.forEach {
            specialOffsets[it] = offsetInfo
        }
        return this
    }

    fun withOffset(
        left: Int,
        right: Int,
        top: Int,
        bottom: Int
    ): UniversalDecorator {
        defaultOffsetInfo = OffsetInfo(left.dpToPx, right.dpToPx, top.dpToPx, bottom.dpToPx)
        return this
    }

    fun withDefaultDivider(
        context: Context,
        vararg layouts: Int
    ): UniversalDecorator {
        val styledAttributes =
            context.obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
        defaultDivider = styledAttributes.getDrawable(0)
        styledAttributes.recycle()
        viewTypesDefaultDivider = layouts.toHashSet()
        return this
    }

    override fun onDraw(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val divider = colorDivider ?: return
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            tryDrawChildDivider(canvas, parent, index, divider)
        }
    }

    private fun tryDrawChildDivider(
        canvas: Canvas,
        parent: RecyclerView,
        childIndex: Int,
        divider: Drawable
    ) {
        val child = parent.getChildAt(childIndex)
        val nextChild = parent.getChildAt(childIndex + 1) ?: return

        val viewHolder = parent.getChildViewHolder(child)
        val nextViewHolder = parent.getChildViewHolder(nextChild)
        val viewType = viewHolder.itemViewType
        val nextViewType = nextViewHolder.itemViewType
        if (dividedPairs[viewType] == nextViewType || dividedPairs[viewType] == ANY_LAYOUT) {
            val offsetInfo = if (hasOverride(viewType, nextViewType)) {
                getOverrideOffsets(viewType, nextViewType)?.first
            } else {
                specialOffsets[viewType]
            }

            val dividerTop = child.bottom + (offsetInfo?.bottom ?: 0)
            val dividerBottom = dividerTop + gapSize
            val dividerLeft = parent.paddingLeft
            val dividerRight = parent.width - parent.paddingRight
            divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
            divider.draw(canvas)
        }
    }

    override fun onDrawOver(
        c: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (viewTypesDefaultDivider.isNullOrEmpty()) return
        val divider = defaultDivider ?: return
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val viewHolder = parent.getChildViewHolder(child)

            if (viewTypesDefaultDivider!!.contains(viewHolder.itemViewType)) {
                val dividerBottom = child.bottom + child.translationY.roundToInt()
                val dividerTop = dividerBottom - 1.dpToPx

                divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                divider.draw(c)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)

        parent.adapter?.let {
            val holderPrevLayout = it.getItemViewType(position - 1)
            val holderCurrentLayout = it.getItemViewType(position)
            val holderNextLayout = it.getItemViewType(position + 1)

            if (position == 0) {
                gapsBeforeFirst[holderCurrentLayout]?.let {
                    outRect.top = it
                }
            }

            if (dividedPairs[holderCurrentLayout] == holderNextLayout || dividedPairs[holderCurrentLayout] == ANY_LAYOUT) {
                outRect.bottom += gapSize
            }

            val offsetInfo = when {
                hasOverride(holderCurrentLayout, holderNextLayout) -> {
                    getOverrideOffsets(holderCurrentLayout, holderNextLayout)?.first
                }
                hasOverride(holderPrevLayout, holderCurrentLayout) -> {
                    getOverrideOffsets(holderPrevLayout, holderCurrentLayout)?.second
                }
                specialOffsets.containsKey(holderCurrentLayout) -> {
                    specialOffsets[holderCurrentLayout]
                }
                viewTypes == null || viewTypes?.contains(holderCurrentLayout) == true -> {
                    defaultOffsetInfo
                }
                else -> null
            }

            offsetInfo?.also {
                outRect.left = it.left
                outRect.right = it.right
                outRect.top += it.top
                outRect.bottom += it.bottom
            }
        }
    }

    private fun hasOverride(firstType: Int, secondType: Int): Boolean {
        return overridePairs[firstType] == secondType
    }

    private fun getOverrideOffsets(firstType: Int, secondType: Int): Pair<OffsetInfo, OffsetInfo>? {
        return overrideOffsets[getOverrideKey(firstType, secondType)]
    }

    private fun getOverrideKey(firstType: Int, secondType: Int): Int {
        return SHIFT_MULTIPLIER * firstType + secondType
    }

    private fun OffsetInfo.pxToDp() =
        OffsetInfo(left.dpToPx, right.dpToPx, top.dpToPx, bottom.dpToPx)

    data class OffsetInfo(
        val left: Int,
        val right: Int,
        val top: Int,
        val bottom: Int
    )
}