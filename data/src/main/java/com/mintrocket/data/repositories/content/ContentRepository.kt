package com.mintrocket.data.repositories.content

import com.mintrocket.data.model.domain.ContentPost
import com.mintrocket.datacore.model.PaginationPage

interface ContentRepository {

    suspend fun getContentPosts(page: Int, perPage: Int): PaginationPage<ContentPost>
}