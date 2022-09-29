package com.mintrocket.baseproject.debug_screen.features

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.ItemDebugFeatureSectionBinding
import com.mintrocket.uicore.recycler.generateId

class ItemFeatureSection(
    private val title: String,
) : AbstractBindingItem<ItemDebugFeatureSectionBinding>() {

    override val type: Int
        get() = R.id.item_debug_feature_section

    override var identifier: Long
        get() = generateId(title)
        set(value) {}

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemDebugFeatureSectionBinding {
        return ItemDebugFeatureSectionBinding.inflate(inflater, parent, false)
    }

    override fun bindView(binding: ItemDebugFeatureSectionBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.tvTitle.text = title
    }
}