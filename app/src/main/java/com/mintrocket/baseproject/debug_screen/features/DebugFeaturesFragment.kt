package com.mintrocket.baseproject.debug_screen.features

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mintrocket.baseproject.MainActivity
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.FragmentDebugFeaturesBinding
import com.mintrocket.data.feature_toggling.features.toggles.LocalFeatureToggle
import com.mintrocket.data.feature_toggling.features.toggles.RemoteFeatureToggle
import com.mintrocket.data.feature_toggling.provider.FeatureConfigsProvider
import com.mintrocket.data.feature_toggling.repository.state.FeaturesStateRepository
import com.mintrocket.uicore.withNewTaskFlag
import org.koin.android.ext.android.inject

class DebugFeaturesFragment : Fragment(R.layout.fragment_debug_features) {

    private val featuresAdapter = ItemAdapter<GenericItem>()
    private val fastAdapter = FastAdapter.with(featuresAdapter)

    private val localFeatureController by inject<FeaturesStateRepository>()
    private val featureConfigsProvider by inject<FeatureConfigsProvider>()

    private val binding by viewBinding<FragmentDebugFeaturesBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding.rvFeatures) {
            layoutManager = LinearLayoutManager(context)
            adapter = fastAdapter
        }
        binding.btApply.setOnClickListener {
            startActivity(
                Intent(requireContext(), MainActivity::class.java).withNewTaskFlag()
            )
        }
        refreshFeaturesList()
    }

    private fun refreshFeaturesList() {
        val remoteFeatures = RemoteFeatureToggle.values().map {
            ItemDebugFeatureToggle(
                it.key,
                it.key,
                localFeatureController.getState(it.key),
                featureConfigsProvider.isRemoteEnabled(it)
            ) { newState ->
                localFeatureController.setState(it.key, newState)
                refreshFeaturesList()
            }
        }
        val localFeatures = LocalFeatureToggle.values().map {
            ItemDebugFeatureToggle(
                it.title,
                it.key,
                localFeatureController.getState(it.key),
                featureConfigsProvider.isLocalEnabled(it)
            ) { newState ->
                localFeatureController.setState(it.key, newState)
                refreshFeaturesList()
            }
        }
        val listItem = mutableListOf<GenericItem>().apply {
            add(ItemFeatureSection(getString(R.string.debug_feature_section_local)))
            addAll(localFeatures)
            add(ItemFeatureSection(getString(R.string.debug_feature_section_remote)))
            addAll(remoteFeatures)
        }
        featuresAdapter.setNewList(listItem)
    }
}