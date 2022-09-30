package com.mintrocket.mobile.screens.contentlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.addClickListener
import com.mikepenz.fastadapter.scroll.EndlessRecyclerOnScrollListener
import com.mintrocket.data.model.domain.ContentPost
import com.mintrocket.mobile.R
import com.mintrocket.mobile.common.ItemLoadingMore
import com.mintrocket.mobile.databinding.FragmentContentListBinding
import com.mintrocket.mobile.di.scopeViewModel
import com.mintrocket.mobile.screens.contentlist.adapter.ContentItemsBuilder
import com.mintrocket.uicore.extraNotNull
import com.mintrocket.uicore.manageProgressWithRefresh
import com.mintrocket.uicore.observeEvent
import com.mintrocket.uicore.recycler.UniversalDecorator
import com.mintrocket.uicore.withArgs
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContentListFragment : Fragment(R.layout.fragment_content_list) {

    companion object {
        private const val CONTENT_ITEM_HORIZONTAL_OFFSET = 16
        private const val CONTENT_ITEM_VERTICAL_OFFSET = 16
    }

    private val itemsBuilder by inject<ContentItemsBuilder>()
    private val viewModel by inject<ContentListViewModel>()
    private val contentAdapter = ItemAdapter<GenericItem>()
    private val loadingMoreAdapter = ItemAdapter<GenericItem>()
    private val fastAdapter = FastAdapter.with(contentAdapter)
        .addAdapter(1, loadingMoreAdapter)

    private var endlessScrollListener: EndlessRecyclerOnScrollListener? = null

    private val binding by viewBinding<FragmentContentListBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initSubscriptions()
    }

    private fun initView() {

        binding.rvMain.apply {
            adapter = fastAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                UniversalDecorator().withOffset(
                    CONTENT_ITEM_HORIZONTAL_OFFSET,
                    CONTENT_ITEM_HORIZONTAL_OFFSET,
                    CONTENT_ITEM_VERTICAL_OFFSET,
                    CONTENT_ITEM_VERTICAL_OFFSET
                )
            )
        }

        fastAdapter.addClickListener(
            resolveView = { vh: ItemLoadingMore.ViewHolder -> vh.binding.btRetry },
            onClick = { _, _, _, _ ->
                viewModel.retryLoadPage()
            }
        )

        binding.swipeMain.setOnRefreshListener { viewModel.reload() }
        resetEndlessScroll()
    }

    private fun initSubscriptions() {
        with(viewModel) {
            progress.observe(viewLifecycleOwner, ::onProgress)
            allPosts().observe(viewLifecycleOwner, ::onInitialPageLoaded)
            postsPage().observeEvent(viewLifecycleOwner, ::onNextPageLoaded)
        }
    }

    private fun onInitialPageLoaded(posts: List<ContentPost>) {
        itemsBuilder.buildItems(
            viewLifecycleOwner.lifecycleScope,
            posts,
            contentAdapter
        )
        resetEndlessScroll()
    }

    private fun onNextPageLoaded(posts: List<ContentPost>) {
        itemsBuilder.buildPageItems(
            viewLifecycleOwner.lifecycleScope,
            posts,
            contentAdapter
        )
    }

    private fun onProgress(showProgress: Boolean) {
        manageProgressWithRefresh(showProgress, binding.progressMain, binding.swipeMain)
    }

    /**
     * EndlessScrollListener doesn't work nice when data in rv is changed
     * and amount of items is reduced.
     */
    private fun resetEndlessScroll() {
        endlessScrollListener?.let {
            binding.rvMain.removeOnScrollListener(it)
        }

        endlessScrollListener = object : EndlessRecyclerOnScrollListener(1) {
            override fun onLoadMore(currentPage: Int) {
                if (loadingMoreAdapter.itemList.isEmpty) {
                    loadingMoreAdapter.add(
                        ItemLoadingMore(
                            R.string.all_more_posts,
                            viewModel.loadMoreState
                        )
                    )
                }
                viewModel.loadMore()
            }
        }.also {
            binding.rvMain.addOnScrollListener(it)
        }
    }
}



