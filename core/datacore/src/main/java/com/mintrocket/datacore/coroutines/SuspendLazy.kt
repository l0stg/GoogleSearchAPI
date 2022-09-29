package com.mintrocket.datacore.coroutines

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class SuspendLazy<T>(
    private val fetcher: suspend () -> T
) {
    private var value: T? = null
    private val mutex = Mutex()

    suspend fun get(): T {
        return mutex.withLock {
            value ?: fetcher.invoke().apply {
                value = this
            }
        }
    }
}