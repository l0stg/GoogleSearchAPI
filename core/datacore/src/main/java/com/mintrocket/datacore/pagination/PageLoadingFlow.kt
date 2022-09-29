package com.mintrocket.datacore.pagination

import com.mintrocket.datacore.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

open class PageLoadingFlow<R, C, P>(
    scope: CoroutineScope,
    private val startPage: P,
    private val fetcher: suspend (P) -> R,
    private val nexPageStrategy: (R) -> P?,
    private val contentListStrategy: (R) -> List<C>
) {
    private val pageNumberFlow = MutableStateFlow(PageTrigger(startPage))
    private val loadingFlow = pageNumberFlow.flatMapLatest { trigger ->
        flow {
            if (trigger.page != null) {
                emit(PageLoadingState.loading<R, P?>(trigger.page))
                emit(PageLoadingState.success<R, P?>(fetcher(trigger.page), trigger.page))
            } else {
                emit(PageLoadingState.empty<R, P>())
            }
        }.catch {
            emit(PageLoadingState.failed(it, trigger.page))
        }
    }
        .conflate()
        .shareIn(scope, SharingStarted.Lazily) // todo need rewrite

    private var nextPage: P? = null

    private val allItemsFlow = MutableStateFlow<MutableList<C>?>(null)
    private val pageItemsFlow = MutableStateFlow<Event<List<C>>?>(null)

    init {
        loadingFlow.filter { it.isSuccess() }
            .onEach {
                nextPage = nexPageStrategy(it.requireContent())
                val contentList = contentListStrategy(it.requireContent())
                if (it.page == startPage) {
                    allItemsFlow.value = contentList.toMutableList()
                } else {
                    allItemsFlow.value?.addAll(contentList)
                    pageItemsFlow.value = Event(contentList)
                }
            }.launchIn(scope)
    }

    fun allItemsFlow(): Flow<List<C>> = allItemsFlow.filterNotNull()

    fun pageItemsFlow(): Flow<Event<List<C>>> = pageItemsFlow.filterNotNull()

    fun reload() {
        pageNumberFlow.value = PageTrigger(startPage)
    }

    fun loadNextPage() {
        pageNumberFlow.value = PageTrigger(nextPage)
    }

    fun retryLoadPage() {
        pageNumberFlow.value = pageNumberFlow.value.copy()
    }

    fun initialLoadingErrorsFlow() = loadingFlow
        .filter { it.page == startPage && it.isFailed() }
        .map { it.requireException() }

    fun initialProgressFlow() = loadingFlow
        .map { it.isLoading() && it.page == startPage }

    fun nextPageLoadingStateFlow() = loadingFlow.map {
        when {
            it.page == startPage -> NextPageLoadingState.IDLE
            it.isEmpty() -> NextPageLoadingState.END_OF_LIST
            it.isLoading() -> NextPageLoadingState.PROGRESS
            it.isFailed() -> NextPageLoadingState.ERROR
            else -> NextPageLoadingState.IDLE
        }
    }

    fun rawFlow() = loadingFlow
}