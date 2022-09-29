package com.mintrocket.datacore

class CallResult<T>(val result: T?, val exception: Throwable?) {

    companion object {
        fun <T> success(result: T) =
            CallResult(result, null)

        fun <T> failure(exeption: Throwable): CallResult<T> =
            CallResult(null, exeption)
    }

    inline fun onSuccess(action: (T) -> Unit): CallResult<T> {
        result?.let { action.invoke(it) }
        return this
    }

    inline fun onError(action: (Throwable) -> Unit): CallResult<T> {
        exception?.let { action.invoke(it) }
        return this
    }
}