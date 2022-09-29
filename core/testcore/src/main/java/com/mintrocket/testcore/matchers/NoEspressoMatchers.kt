package com.mintrocket.testcore.matchers

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.junit.Assert.assertEquals

object NoEspressoMatchers {

    fun RecyclerView.withHoldersCount(count: Int) {
        val realCount = adapter?.itemCount
        assertEquals(
            "Count of holders in a RecyclerView doesn't match expected value",
            count, realCount
        )
    }

    // todo update for using with the FastAdapter
    fun RecyclerView.withCountHoldersOfType(clazz: Class<*>, count: Int) {
        /*val adapter = adapter as? FlexibleAdapter<*>
        val realCount = adapter?.currentItems?.filter {
            it.javaClass == clazz
        }?.count()
        assertEquals(
            "Count of holders with type ${clazz.simpleName} doesn't match expected value",
            count, realCount
        )*/
    }

    fun RecyclerView.holderViewAtPosition(position: Int): View {
        scrollToPosition(position)
        return findViewHolderForAdapterPosition(position)!!.itemView
    }

    fun TextView.hasText(text: String) {
        assertEquals(text, this.text)
    }

    fun View.hasVisibility(visibility: Int): View {
        assertEquals(visibility, this.visibility)
        return this
    }

    fun CheckBox.assertChecked(checked: Boolean): CheckBox {
        assertEquals(isChecked, checked)
        return this
    }
}