package com.mintrocket.baseproject.debug_screen.environment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.ItemDebugEnvironmentBinding
import com.mintrocket.baseproject.debug_screen.environment.data.EnvironmentEntry
import com.mintrocket.uicore.onClick
import com.mintrocket.uicore.recycler.generateId

data class ItemEnvironment(
    private val data: EnvironmentEntry,
    private val selected: Boolean,
    private val clickListener: (EnvironmentEntry) -> Unit,
    private val editClickListener: (EnvironmentEntry) -> Unit,
    private val deleteClickListener: (EnvironmentEntry) -> Unit,
) : AbstractBindingItem<ItemDebugEnvironmentBinding>() {

    override val type: Int = R.id.item_debug_environment

    override var identifier: Long
        get() = generateId(data.id)
        set(value) {}

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemDebugEnvironmentBinding = ItemDebugEnvironmentBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemDebugEnvironmentBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.tvName.text = data.name
        binding.tvUrl.text = data.url
        binding.rbSelection.isChecked = selected
        binding.btEdit.isVisible = data.editable
        binding.btDelete.isVisible = data.editable

        binding.root.onClick { clickListener.invoke(data) }
        binding.btEdit.onClick { editClickListener.invoke(data) }
        binding.btDelete.onClick { deleteClickListener.invoke(data) }
    }
}