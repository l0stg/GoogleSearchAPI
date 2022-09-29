package com.mintrocket.datacore

data class ContentLoadingState<T>(
    val state: State,
    val content: T? = null,
    val exception: Throwable? = null
) {
    enum class State {
        LOADING,
        SUCCESS,
        FAILED
    }

    fun isLoading() = state == State.LOADING

    fun isSuccess() = state == State.SUCCESS

    fun isFailed() = state == State.FAILED

    fun requireException() = exception!!

    fun requireContent() = content!!

    companion object {
        fun <P> failed(exception: Throwable) = ContentLoadingState<P>(
            State.FAILED,
            exception = exception
        )

        fun <P> loading() = ContentLoadingState<P>(State.LOADING)

        fun <P> success(result: P) = ContentLoadingState(
            State.SUCCESS,
            result
        )
    }
}
