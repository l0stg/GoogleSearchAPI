package com.mintrocket.uicore.recycler

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mintrocket.datacore.coroutines.SerialJob
import com.mintrocket.datacore.extensions.into
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class ItemBuilder<T> {

    private val creatingJob = SerialJob()

    open fun buildItems(
        scope: CoroutineScope,
        data: T,
        adapter: ItemAdapter<GenericItem>,
        recyclerView: RecyclerView? = null
    ) {
        scope.launch(Dispatchers.Main) {
            val items = withContext(Dispatchers.IO) {
                createItems(data)
            }
            adapter.setNewList(items)
            recyclerView?.isVisible = true
        } into creatingJob
    }

    open fun buildPageItems(
        scope: CoroutineScope,
        data: T,
        adapter: ItemAdapter<GenericItem>,
        onBuiltCallback: () -> Unit = {}
    ) {
        preparePage(data, adapter)
        scope.launch(Dispatchers.Main) {
            val items = withContext(Dispatchers.IO) {
                createItems(data)
            }
            adapter.add(items)
            onBuiltCallback.invoke()
        } into creatingJob
    }

    abstract fun createItems(data: T): List<GenericItem>

    open fun preparePage(data: T, adapter: ItemAdapter<GenericItem>) {

    }
}