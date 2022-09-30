package com.mintrocket.mobile.screens.contentlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.data.model.domain.ContentPost
import com.mintrocket.data.repositories.content.ContentRepository
import com.mintrocket.datacore.Event
import com.mintrocket.datacore.pagination.ListPageLoadingFlow
import com.mintrocket.datacore.pagination.PageData
import com.mintrocket.navigation.navigator.ScopedNavigator
import com.mintrocket.uicore.toLiveData
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ContentListViewModel(
    private val contentRepository: ContentRepository,
) : ViewModel() {

    companion object {
        private const val INITIAL_PAGE = 1
        private const val PER_PAGE = 25
    }

    private val postsLoadingFlow = ListPageLoadingFlow(viewModelScope, INITIAL_PAGE) {
        contentRepository.getContentPosts(it, PER_PAGE)
        val content = contentRepository.getContentPosts(it, PER_PAGE)
        val nextPage =
            (content.paginationMeta.currentPage + 1).takeIf { it < content.paginationMeta.lastPage }
        PageData(content.page, nextPage)
    }

    val loadMoreState = postsLoadingFlow
        .nextPageLoadingStateFlow()
        .toLiveData(viewModelScope.coroutineContext)

    val progress = postsLoadingFlow.initialProgressFlow()
        .toLiveData(viewModelScope.coroutineContext)

    fun allPosts(): LiveData<List<ContentPost>> = postsLoadingFlow
        .allItemsFlow()
        .toLiveData(viewModelScope.coroutineContext)

    fun postsPage(): LiveData<Event<List<ContentPost>>> = postsLoadingFlow
        .pageItemsFlow()
        .toLiveData(viewModelScope.coroutineContext)

    fun loadMore() {
        postsLoadingFlow.loadNextPage()
    }

    fun reload() {
        postsLoadingFlow.reload()
    }

    fun retryLoadPage() {
        postsLoadingFlow.retryLoadPage()
    }
}