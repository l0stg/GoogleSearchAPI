package com.mintrocket.data.analytics

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class LifecycleTimeCounter(
    private val resultTimeListener: (Long) -> Unit = {}
) : LifecycleObserver {

    private val timeCounter by lazy { TimeCounter() }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resume() {
        timeCounter.start()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pause() {
        resultTimeListener(timeCounter.elapsed())
        timeCounter.stop()
    }
}