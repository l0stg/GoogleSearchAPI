package com.mintrocket.mobile.screens.contentlist.adapter

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mintrocket.mobile.R
import com.mintrocket.mobile.databinding.ItemContentBinding
import com.mintrocket.uicore.recycler.generateId

class ContentItem(
    val itemId: Int
) : AbstractItem<ContentItem.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_content

    override val type: Int
        get() = R.id.item_content

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override var identifier: Long
        get() = generateId(itemId)
        set(value) {}

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<ContentItem>(itemView) {

        val binding by viewBinding<ItemContentBinding>()

        override fun bindView(item: ContentItem, payloads: List<Any>) {
            binding.tvName.text = "Content post ${item.itemId}"
        }

        override fun unbindView(item: ContentItem) {
            // Do nothing
        }
    }
}
