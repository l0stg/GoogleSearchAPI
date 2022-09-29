package com.mintrocket.baseproject.debug_screen.features

import android.graphics.Color
import android.view.View
import androidx.core.content.ContextCompat
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.ItemDebugFeatureToggleBinding
import com.mintrocket.data.feature_toggling.repository.state.FeatureEnabledState
import com.mintrocket.uicore.recycler.generateId

class ItemDebugFeatureToggle(
    val name: String,
    val key: String,
    val featureState: FeatureEnabledState,
    val featureEnabled: Boolean,
    val stateClickListener: (FeatureEnabledState) -> Unit
) : AbstractItem<ItemDebugFeatureToggle.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_debug_feature_toggle

    override val type: Int
        get() = R.id.item_debug_feature_toggle

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    override var identifier: Long
        get() = generateId(key)
        set(value) {}

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<ItemDebugFeatureToggle>(itemView) {

        val binding by viewBinding<ItemDebugFeatureToggleBinding>()

        override fun bindView(item: ItemDebugFeatureToggle, payloads: List<Any>) {
            val stateTextRes = if (item.featureEnabled) {
                R.string.debug_feature_state_enabled
            } else {
                R.string.debug_feature_state_disabled
            }
            val stateColorRes = if (item.featureEnabled) {
                R.color.debug_feature_enabled
            } else {
                R.color.debug_feature_disabled
            }
            val stateColor = ContextCompat.getColor(binding.root.context, stateColorRes)
            binding.tvFeatureName.text = item.name
            binding.tvState.setText(stateTextRes)
            binding.tvState.setTextColor(stateColor)

            when (item.featureState) {
                FeatureEnabledState.DEFAULT -> binding.btDefault.isChecked = true
                FeatureEnabledState.ENABLED -> binding.btEnabled.isChecked = true
                FeatureEnabledState.DISABLED -> binding.btDisabled.isChecked = true
                else -> binding.tgStates.clearChecked()
            }

            binding.btDefault.setOnClickListener {
                item.stateClickListener.invoke(FeatureEnabledState.DEFAULT)
            }
            binding.btEnabled.setOnClickListener {
                item.stateClickListener.invoke(FeatureEnabledState.ENABLED)
            }
            binding.btDisabled.setOnClickListener {
                item.stateClickListener.invoke(FeatureEnabledState.DISABLED)
            }
        }

        override fun unbindView(item: ItemDebugFeatureToggle) {
            // Do nothing
        }
    }
}