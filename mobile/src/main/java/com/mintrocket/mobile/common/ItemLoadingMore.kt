package com.mintrocket.mobile.common

import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.mintrocket.datacore.pagination.NextPageLoadingState
import com.mintrocket.mobile.R
import com.mintrocket.mobile.databinding.ItemLoadingMoreBinding
import kotlinx.coroutines.*

class ItemLoadingMore(
    @StringRes val titleRes: Int,
    val progressLD: LiveData<NextPageLoadingState>
) : AbstractItem<ItemLoadingMore.ViewHolder>() {

    override val layoutRes: Int
        get() = R.layout.item_loading_more

    override val type: Int
        get() = R.id.item_loading_more

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View) : FastAdapter.ViewHolder<ItemLoadingMore>(itemView) {

        companion object{
            private const val END_OF_LIST_DELAY = 1000L
        }

        private var scope: CoroutineScope? = null

        private val observer = Observer<NextPageLoadingState> {
            binding.groupLoading.isVisible = it == NextPageLoadingState.PROGRESS
            binding.groupRetry.isVisible = it == NextPageLoadingState.ERROR
            binding.tvEndOfList.isVisible = it == NextPageLoadingState.END_OF_LIST

            if (it == NextPageLoadingState.END_OF_LIST) {
                scope?.launch {
                    delay(END_OF_LIST_DELAY)
                    binding.tvEndOfList.isVisible = false
                }
            }
        }

        val binding by viewBinding<ItemLoadingMoreBinding>()

        override fun bindView(item: ItemLoadingMore, payloads: List<Any>) {
            scope = CoroutineScope(Dispatchers.Main)
            item.progressLD.observeForever(observer)
            binding.tvLoadMore.setText(item.titleRes)
        }

        override fun unbindView(item: ItemLoadingMore) {
            scope?.cancel()
            scope = null
            item.progressLD.removeObserver(observer)
        }
    }
}