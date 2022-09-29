package com.mintrocket.datacore.pagination

class PageLoadingState<T, P>(
    val state: State,
    val page: P?,
    val content: T? = null,
    val exception: Throwable? = null
) {
    enum class State {
        LOADING,
        SUCCESS,
        FAILED,
        EMPTY
    }

    fun isLoading() = state == State.LOADING

    fun isSuccess() = state == State.SUCCESS

    fun isFailed() = state == State.FAILED

    fun isEmpty() = state == State.EMPTY

    fun requireException() = exception!!

    fun requireContent() = content!!

    companion object {
        fun <T, P> failed(
            exception: Throwable,
            page: P?
        ) = PageLoadingState<T, P>(
            State.FAILED,
            page,
            exception = exception
        )

        fun <T, P> loading(page: P) =
            PageLoadingState<T, P>(State.LOADING, page)

        fun <T, P> success(
            result: T,
            page: P
        ) = PageLoadingState(
            State.SUCCESS,
            page,
            result
        )

        fun <T, P> empty() = PageLoadingState<T, P>(
            State.EMPTY,
            null,
        )
    }
}
