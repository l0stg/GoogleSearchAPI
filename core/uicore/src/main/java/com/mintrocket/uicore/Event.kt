package com.mintrocket.uicore

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mintrocket.datacore.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline onChanged: (T) -> Unit
): Observer<Event<T>> {
    val wrappedObserver = Observer<Event<T>> { t -> t.content()?.let { onChanged.invoke(it) } }
    observe(owner, wrappedObserver)
    return wrappedObserver
}

fun <T> MutableLiveData<Event<T>>.postEvent(value: T) {
    postValue(Event(value))
}

var <T> MutableLiveData<Event<T>>.event: T?
    set(v) {
        value = v?.let { Event(v) }
    }
    get() = value?.content

// set event only if LiveData has active observers
var <T> MutableLiveData<Event<T>>.activeEvent: T?
    set(v) {
        if (hasActiveObservers()) {
            value = v?.let { Event(v) }
        }
    }
    get() = value?.content

fun <T> Flow<Event<T>>.onEachEvent(action: suspend (T) -> Unit): Flow<Event<T>> =
    onEach {
        it.content()?.let { action(it) }
    }