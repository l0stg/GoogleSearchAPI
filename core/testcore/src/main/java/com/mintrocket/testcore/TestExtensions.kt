package com.mintrocket.testcore

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions
import com.agoda.kakao.delegate.ViewInteractionDelegate
import com.mintrocket.testcore.matchers.RecyclerViewMatcher
import org.hamcrest.Matcher
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

fun onRecycler(rvId: Int) = RecyclerViewMatcher(rvId)

fun RecyclerViewMatcher.position(position: Int): ViewInteraction {
    return Espresso.onView(this.atPosition(position))
}

fun RecyclerViewMatcher.positionAndView(position: Int, viewID: Int): ViewInteraction {
    return Espresso.onView(this.atPositionOnView(position, viewID))
}

fun RecyclerViewMatcher.typeAndView(itemClass: Class<*>, viewID: Int): ViewInteraction {
    return Espresso.onView(this.withTypeOnView(itemClass, viewID))
}

fun RecyclerViewMatcher.typeAndOrder(itemClass: Class<*>, order: Int): ViewInteraction {
    return Espresso.onView(this.withTypeAndPositionOnView(itemClass, order, -1))
}

fun RecyclerViewMatcher.typeAndOrderOnView(
    itemClass: Class<*>,
    position: Int,
    viewID: Int
): ViewInteraction {
    return Espresso.onView(withTypeAndPositionOnView(itemClass, position, viewID))
}

// todo update for using with the FastAdapter
fun RecyclerView.indexOfFirstItemWithType(itemClass: Class<*>): Int {
    return -1 //(adapter as FlexibleAdapter<*>).currentItems.indexOfFirst { it.javaClass == itemClass }
}

fun ViewInteractionDelegate.checkMatches(viewMatcher: Matcher<in View>) =
    check(ViewAssertions.matches(viewMatcher))

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    return data as T
}
