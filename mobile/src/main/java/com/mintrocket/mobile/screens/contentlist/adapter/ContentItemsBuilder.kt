package com.mintrocket.mobile.screens.contentlist.adapter

import com.mikepenz.fastadapter.GenericItem
import com.mintrocket.data.model.domain.ContentPost
import com.mintrocket.uicore.recycler.ItemBuilder

class ContentItemsBuilder : ItemBuilder<List<ContentPost>>() {

    override fun createItems(data: List<ContentPost>): List<GenericItem> {
        return data.map { ContentItem(it.id) }
    }
}