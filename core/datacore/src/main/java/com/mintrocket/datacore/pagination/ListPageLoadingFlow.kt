package com.mintrocket.datacore.pagination

import kotlinx.coroutines.CoroutineScope

class ListPageLoadingFlow<T, P>(
    scope: CoroutineScope,
    startPage: P,
    fetcher: suspend (P) -> PageData<T, P>
) : PageLoadingFlow<PageData<T, P>, T, P>(
    scope,
    startPage,
    fetcher,
    { it.nextPage },
    { it.content }
)

