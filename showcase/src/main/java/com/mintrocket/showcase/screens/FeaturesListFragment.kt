package com.mintrocket.showcase.screens

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.showcase.AuthenticationLaunchScreen
import com.mintrocket.showcase.R
import com.mintrocket.showcase.databinding.FragmentFeaturesListBinding
import com.mintrocket.uicore.recycler.UniversalDecorator
import org.koin.android.ext.android.inject

class FeaturesListFragment : Fragment(R.layout.fragment_features_list) {

    companion object {
        private const val ITEM_HORIZONTAL_OFFSET = 16
        private const val ITEM_TOP_OFFSET = 8
        private const val ITEM_BOTTOM_OFFSET = 4

        fun newInstance() = FeaturesListFragment()
    }

    private val navigator by inject<ApplicationNavigator>()

    private val featuresAdapter = ItemAdapter<ItemFeature>()
    private val fastAdapter = FastAdapter.with(featuresAdapter)

    private val binding by viewBinding<FragmentFeaturesListBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarFeatures.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(
                top = insets.systemWindowInsetTop
            )
            insets
        }

        with(binding.rvFeatures) {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = fastAdapter
            addItemDecoration(
                UniversalDecorator()
                    .withOffset(
                        ITEM_HORIZONTAL_OFFSET,
                        ITEM_HORIZONTAL_OFFSET,
                        ITEM_TOP_OFFSET,
                        ITEM_BOTTOM_OFFSET
                    )
            )
        }

        featuresAdapter.setNewList(
            Features.values().map { ItemFeature(it) }
        )

        fastAdapter.onClickListener = { _, _, item, _ ->
            when (item.feature) {
                Features.AUTHENTICATION -> navigator.navigateTo(AuthenticationLaunchScreen())
            }
            true
        }
    }
}