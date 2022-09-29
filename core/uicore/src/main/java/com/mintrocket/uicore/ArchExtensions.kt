package com.mintrocket.uicore

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext


// same as asLiveData but with max timeout and required context
fun <T> Flow<T>.toLiveData(
    context: CoroutineContext
): LiveData<T> = this.asLiveData(context, Long.MAX_VALUE)