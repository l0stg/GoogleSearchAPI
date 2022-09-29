package com.mintrocket.baseproject.debug_screen.logreader

import android.view.LayoutInflater
import android.view.ViewGroup
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.ItemDebugLogLineBinding
import com.mintrocket.baseproject.debug_screen.logreader.data.LogCatLine
import com.mintrocket.uicore.onClick
import com.mintrocket.uicore.recycler.generateId

data class ItemDebugLogLine(
    private val data: LogCatLine,
    private val clickListener: (LogCatLine) -> Unit,
    private val longClickListener: (LogCatLine) -> Unit
) : AbstractBindingItem<ItemDebugLogLineBinding>() {

    override val type: Int = R.id.item_debug_log_line

    override var identifier: Long
        get() = generateId(data.id)
        set(value) {}

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemDebugLogLineBinding = ItemDebugLogLineBinding.inflate(inflater, parent, false)

    override fun bindView(binding: ItemDebugLogLineBinding, payloads: List<Any>) {
        super.bindView(binding, payloads)
        binding.tvMeta.text = "${data.toLevelName()} · ${data.formatDate()} · ${data.tag}"
        binding.tvMessage.text = data.message
        binding.tvMeta.setTextColor(data.toLevelColor(binding.tvMeta.context))
        binding.root.onClick { clickListener.invoke(data) }
        binding.root.setOnLongClickListener {
            longClickListener.invoke(data)
            true
        }
    }

}