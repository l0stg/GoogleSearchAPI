package com.mintrocket.testcore.matchers

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import com.agoda.kakao.recycler.KRecyclerItem
import com.agoda.kakao.recycler.KRecyclerView
import com.mintrocket.testcore.checkMatches
import com.mintrocket.testcore.matchers.CustomActions.stupidClick
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers

fun KRecyclerView.withCountHoldersOfType(
    clazz: Class<*>,
    count: Int
) {
    view.checkMatches(CustomMatchers.withCountHoldersOfType(clazz, count))
}

fun KRecyclerView.withFirstHolderOfType(clazz: Class<*>) {
    view.checkMatches(CustomMatchers.withFirstHolderOfType(clazz))
}

interface KCustomActions {
    val view: ViewInteraction

    fun clickStupid() {
        view.perform(stupidClick())
    }
}

inline fun <reified T : KRecyclerItem<*>> KRecyclerView.childOfType(
    order: Int,
    klass: Class<*>,
    function: T.() -> Unit
) {
    val provideItem = itemTypes.getOrElse(T::class) {
        throw IllegalStateException("${T::class.java.simpleName} did not register to KRecyclerView")
    }.provideItem

    // todo update for using with the FastAdapter
    try {
        /* val position = (this.getAdapter() as FlexibleAdapter<*>).currentItems
             .mapIndexed { index, iFlexible -> index to iFlexible }
             .filter { it.second.javaClass == klass }[order].first

         scrollTo(position)*/

    } catch (error: Throwable) {
    }

    function((provideItem(RecyclerOrderMatcher(matcher, order, klass)) as T)
        .also { inRoot { withMatcher(this@childOfType.root) } })
}

class RecyclerOrderMatcher(
    private val parent: Matcher<View>,
    private val order: Int,
    private val holderClass: Class<*>
) : BoundedMatcher<View, View>(View::class.java) {
    override fun describeTo(desc: Description) {
        desc.appendText("view with class ${holderClass.simpleName} and order $order ")
            .appendDescriptionOf(parent)
    }

    override fun matchesSafely(view: View?): Boolean {
        // todo update for using with the FastAdapter
        view?.let {
            /*if ((parent.matches(it.parent) && it.parent is RecyclerView)
                && (it.parent as RecyclerView).adapter is FlexibleAdapter<*>
            ) {
                val adapter = (it.parent as RecyclerView).adapter as FlexibleAdapter<*>
                val targetItemsList = adapter.currentItems
                    .mapIndexed { index, iFlexible -> index to iFlexible }
                    .filter { it.second.javaClass == holderClass }

                val targetItemPosition = targetItemsList[order].first
                val holder =
                    (it.parent as RecyclerView).findViewHolderForLayoutPosition(targetItemPosition)
                return holder?.itemView == it
            }*/
        }

        return false
    }
}

fun KRecyclerView.getAdapter(): RecyclerView.Adapter<*> {
    var adapter: RecyclerView.Adapter<*>? = null
    view.perform(object : ViewAction {
        override fun getDescription() = ""

        override fun getConstraints() = Matchers.allOf(
            ViewMatchers.isAssignableFrom(RecyclerView::class.java),
            ViewMatchers.isDisplayed()
        )

        override fun perform(uiController: UiController?, view: View?) {
            if (view is RecyclerView) {
                adapter = view.adapter
            }
        }
    })
    return adapter!!
}