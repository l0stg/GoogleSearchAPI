package com.mintrocket.data.repositories.content

import com.mintrocket.data.api.ApplicationApi
import com.mintrocket.data.model.domain.ContentPost
import com.mintrocket.datacore.model.PaginationPage

class ContentRepositoryImpl(private val api: ApplicationApi) : ContentRepository {

    override suspend fun getContentPosts(page: Int, perPage: Int): PaginationPage<ContentPost> {
        return api.getPosts(page, perPage)
    }
}