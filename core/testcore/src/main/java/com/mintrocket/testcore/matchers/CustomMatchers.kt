package com.mintrocket.testcore.matchers

import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.matcher.BoundedMatcher
import com.mikepenz.fastadapter.FastAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object CustomMatchers {

    fun isRefreshing(): Matcher<View> {
        return object : BoundedMatcher<View, SwipeRefreshLayout>(SwipeRefreshLayout::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" SwipeRefreshLayout is refreshing")
            }

            override fun matchesSafely(item: SwipeRefreshLayout?): Boolean {
                return item?.isRefreshing ?: false
            }
        }
    }

    fun withTitle(title: String): Matcher<View> {
        return object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" SwipeRefreshLayout is refreshing")
            }

            override fun matchesSafely(item: Toolbar?): Boolean {
                return item?.title == title
            }
        }
    }

    fun withTitleRes(@StringRes title: Int): Matcher<View> {
        return object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" SwipeRefreshLayout is refreshing")
            }

            override fun matchesSafely(item: Toolbar?): Boolean {
                return item?.let {
                    it.title == it.context.getString(title)
                } ?: false
            }
        }
    }

    fun withItemCount(count: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" item count $count")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val itemCount = item?.childCount
                return count == itemCount
            }
        }
    }

    fun withChildCount(count: Int): Matcher<View> {
        return object : BoundedMatcher<View, ViewGroup>(ViewGroup::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" item count $count")
            }

            override fun matchesSafely(item: ViewGroup?): Boolean {
                val itemCount = item?.childCount
                return count == itemCount
            }
        }
    }

    fun withChildPosition(
        parentMatcher: Matcher<View>,
        childPosition: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with $childPosition child view of type parentMatcher")
            }

            override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) {
                    return parentMatcher.matches(view.parent)
                }

                val group = view.parent as ViewGroup
                return parentMatcher.matches(view.parent) && group.getChildAt(childPosition) == view
            }
        }
    }

    fun withFirstVisible(position: Int): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" first visible item position $position")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val firstPosition =
                    (item?.layoutManager as LinearLayoutManager?)?.findFirstCompletelyVisibleItemPosition()
                return position == firstPosition
            }
        }
    }

    fun hasHolderType(clazz: Class<*>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" has holder with type ${clazz.canonicalName}")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                // todo update for using with the FastAdapter
                /*val adapter = item?.adapter as FlexibleAdapter<*>?
                val items = adapter?.currentItems
                return items?.firstOrNull { it.javaClass == clazz } != null*/
                return false
            }
        }
    }

    fun withHolderType(
        position: Int,
        clazz: Class<*>
    ): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" holder on position $position as type ${clazz.canonicalName}")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                /*val adapter = item?.adapter as FlexibleAdapter<*>?
                val items = adapter?.currentItems
                return items?.get(position)?.javaClass == clazz*/
                return false
            }
        }
    }

    fun withFirstHolderOfType(clazz: Class<*>): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" holder type ${clazz.canonicalName}")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val adapter = item?.adapter as FastAdapter<*>?
                return adapter?.getItem(0)?.javaClass == clazz
            }
        }
    }

    fun withCountHoldersOfType(
        clazz: Class<*>,
        count: Int
    ): Matcher<View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description?.appendText(" holder type ${clazz.canonicalName} and count $count")
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                // todo update for using with the FastAdapter
                /*val adapter = item?.adapter as FlexibleAdapter<*>?
                val items = adapter?.currentItems?.filter { it.javaClass == clazz }
                return items?.size == count*/
                return false
            }
        }
    }
}

