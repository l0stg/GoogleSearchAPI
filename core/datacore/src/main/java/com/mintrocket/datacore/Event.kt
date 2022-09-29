package com.mintrocket.datacore

class Event<T>(val content: T) {

    private var handled = false

    fun content() = if (handled) {
        null
    } else {
        handled = true
        content
    }
}