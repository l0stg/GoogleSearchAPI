package com.mintrocket.testcore.matchers

import android.view.View
import android.widget.CompoundButton
import androidx.core.widget.NestedScrollView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom


object CustomActions {

    fun scrollToEnd(): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "Scroll recyclerview to last position"

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(RecyclerView::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                val rv = view as RecyclerView
                val position = if (rv.adapter != null) {
                    rv.adapter!!.itemCount - 1
                } else {
                    rv.childCount
                }
                rv.scrollToPosition(position)
            }
        }
    }

    fun scrollToHolderWithPosition(holderClass: Class<*>, position: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() =
                "Scroll RecyclerView to holder type with specified order"

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(RecyclerView::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                // todo update for using with the FastAdapter
                /*val rv = view as RecyclerView
                val adapterItems = (rv.adapter as FlexibleAdapter<*>).currentItems
                val targetItem = adapterItems
                    .filter { it.javaClass == holderClass }[position]
                val targetPosition = adapterItems.indexOf(targetItem)

                rv.scrollToPosition(targetPosition)*/
            }
        }
    }

    /**
     * Performs stupid click on view, it doesn't give a fuck is view visible or not
     */
    fun stupidClick(): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "Click and don't give a fuck is view is visible or not"

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(View::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                view!!.performClick()
            }
        }
    }

    fun setChecked(checked: Boolean): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "Set checked $checked"

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(CompoundButton::class.java)
            }

            override fun perform(
                uiController: UiController?,
                view: View?
            ) {
                (view as CompoundButton).isChecked = checked
            }
        }
    }

    fun scrollNestedToBottom(): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "Scroll nestedScrollView to bottom"

            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(NestedScrollView::class.java)
            }

            override fun perform(uiController: UiController?, view: View?) {
                (view as NestedScrollView).fullScroll(View.FOCUS_DOWN)
            }
        }
    }
}