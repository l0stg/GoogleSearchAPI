package com.mintrocket.showcase.screens

import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mintrocket.showcase.R
import com.mintrocket.showcase.databinding.ItemFeatureBinding
import com.mintrocket.uicore.recycler.generateId

class ItemFeature(
    val feature: Features
) : AbstractItem<ItemFeature.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_feature

    override val type: Int
        get() = R.id.item_feature

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override var identifier: Long
        get() = generateId(feature)
        set(value) {}

    class ViewHolder(containerView: View) :
        FastAdapter.ViewHolder<ItemFeature>(containerView) {

        val binding by viewBinding<ItemFeatureBinding>()

        override fun bindView(item: ItemFeature, payloads: List<Any>) {
            binding.tvFeatureName.setText(item.feature.nameRes)
        }

        override fun unbindView(item: ItemFeature) {
            /* no-op */
        }
    }
}