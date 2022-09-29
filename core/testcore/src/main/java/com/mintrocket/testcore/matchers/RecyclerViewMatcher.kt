package com.mintrocket.testcore.matchers

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcher(private val recyclerViewId: Int) {

    companion object {
        fun withRecyclerView(recyclerViewId: Int): RecyclerViewMatcher {
            return RecyclerViewMatcher(recyclerViewId)
        }
    }


    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                if (this.resources != null) {
                    try {
                        idDescription = this.resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        idDescription = String.format(
                            "%s (resource name not found)",
                            arrayOf(Integer.valueOf(recyclerViewId))
                        )
                    }
                }

                description.appendText("with id: $idDescription")
            }

            public override fun matchesSafely(view: View): Boolean {

                this.resources = view.resources

                if (childView == null) {
                    val recyclerView =
                        view.rootView.findViewById<View>(recyclerViewId) as RecyclerView?
                    if (recyclerView != null && recyclerView.id == recyclerViewId) {
                        childView =
                            recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    } else {
                        return false
                    }
                }

                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView = childView!!.findViewById<View>(targetViewId)
                    view === targetView
                }
            }
        }
    }

    /**
     * Use only with flexible adapter
     */
    fun withTypeOnView(clazz: Class<*>, targetViewId: Int): Matcher<View> {
        return withTypeAndPositionOnView(clazz, -1, targetViewId)
    }

    /**
     * Use only with flexible adapter
     */
    fun withTypeAndPositionOnView(clazz: Class<*>, order: Int, targetViewId: Int): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = Integer.toString(recyclerViewId)
                if (this.resources != null) {
                    try {
                        idDescription = this.resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        idDescription = String.format(
                            "%s (resource name not found)",
                            arrayOf(Integer.valueOf(recyclerViewId))
                        )
                    }
                }

                description.appendText("with id: $idDescription")
            }

            public override fun matchesSafely(view: View): Boolean {

                this.resources = view.resources

                //todo update for using with the FastAdapter
                if (childView == null) {
                    /*val recyclerView =
                        view.rootView.findViewById<View>(recyclerViewId) as RecyclerView
                    if (recyclerView.id == recyclerViewId) {
                        val position = if (order == -1) {
                            (recyclerView.adapter as FlexibleAdapter<*>)
                                .currentItems.indexOfFirst { it.javaClass == clazz }
                        } else {
                            val adapter = (recyclerView.adapter as FlexibleAdapter<*>)
                            val items = adapter.currentItems.filter { it.javaClass == clazz }
                            adapter.currentItems.indexOf(items[order])
                        }

                        childView =
                            recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    } else {
                        return false
                    }*/
                    return false
                }


                return if (targetViewId == -1) {
                    view == childView
                } else {
                    val targetView = childView?.findViewById<View>(targetViewId)
                    view == targetView
                }

            }
        }
    }
}
