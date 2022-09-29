package com.mintrocket.baseproject.debug_screen.environment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.ItemDebugEnvironmentAddBinding
import com.mintrocket.uicore.onClick

data class ItemEnvironmentAdd(
    private val clickListener: () -> Unit,
) : AbstractBindingItem<ItemDebugEnvironmentAddBinding>() {

    override val type: Int = R.id.item_debug_environment_add

    override var identifier: Long
        get() = type.hashCode().toLong()
        set(value) {}

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemDebugEnvironmentAddBinding =
        ItemDebugEnvironmentAddBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemDebugEnvironmentAddBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.btAdd.onClick {
            clickListener.invoke()
        }
    }
}