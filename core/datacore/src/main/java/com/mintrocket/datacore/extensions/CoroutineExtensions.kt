package com.mintrocket.datacore.extensions

import com.mintrocket.datacore.coroutines.SerialJob
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

infix fun Job.into(container: SerialJob) = container.set(this)

fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
    var lastEmissionTime = 0L
    collect { upstream ->
        val currentTime = System.currentTimeMillis()
        val emit = currentTime - lastEmissionTime > windowDuration
        if (emit) {
            lastEmissionTime = currentTime
            emit(upstream)
        }
    }
}