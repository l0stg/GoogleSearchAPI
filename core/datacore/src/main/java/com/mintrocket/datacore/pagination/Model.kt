package com.mintrocket.datacore.pagination

data class PageTrigger<T>(
    val page: T?
) {
    // intentionally broken
    override fun equals(other: Any?) = false
}

enum class NextPageLoadingState {
    IDLE,
    PROGRESS,
    ERROR,
    END_OF_LIST
}

data class PageData<T, P>(
    val content: List<T>,
    val nextPage: P?
)