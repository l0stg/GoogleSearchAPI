package com.mintrocket.datacore.model

data class PaginationPage<T>(
    val page: List<T>,
    val paginationMeta: PaginationMeta
)