package com.mintrocket.uicore.views

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/*
When items have different height LinearLayoutManager can not
correctly calculate offset. Use this nice custom layout manager
to avoid that bug
*/
class LinearLayoutManagerWithAccurateOffset(
    context: Context?,
    // if rv attached when already scrolled - layout manager doesn't know children sizes
    private val defaultChildSize: Int = 0
) : LinearLayoutManager(context) {

    // map of child adapter position to its height.
    private val childSizesMap = mutableMapOf<Int, Int>()

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
        for (i in 0 until childCount) {
            val child = getChildAt(i) ?: continue
            childSizesMap[getPosition(child)] = child.height
        }
    }

    override fun computeVerticalScrollOffset(state: RecyclerView.State): Int {
        if (childCount == 0) {
            return 0
        }
        val firstChildPosition = findFirstVisibleItemPosition()
        val firstChild = findViewByPosition(firstChildPosition)
        var scrolledY: Int = -(firstChild?.y?.toInt() ?: 0)
        for (i in 0 until firstChildPosition) {
            scrolledY += childSizesMap[i] ?: defaultChildSize
        }
        return scrolledY
    }

}