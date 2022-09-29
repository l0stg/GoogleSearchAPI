package com.mintrocket.data.feature_toggling.provider

import com.mintrocket.data.feature_toggling.features.toggles.LocalFeatureToggle
import com.mintrocket.data.feature_toggling.features.toggles.RemoteFeatureToggle
import com.mintrocket.data.feature_toggling.features.FeaturesDefaults
import com.mintrocket.data.feature_toggling.repository.remote.RemoteFeaturesRepository
import com.mintrocket.data.feature_toggling.repository.state.FeatureEnabledState
import com.mintrocket.data.feature_toggling.repository.state.FeaturesStateRepository

class FeatureConfigsProviderImpl(
    private val featuresDefaults: FeaturesDefaults,
    private val remoteFeaturesRepository: RemoteFeaturesRepository,
    private val featuresStateRepository: FeaturesStateRepository
) : FeatureConfigsProvider {

    override fun isLocalEnabled(toggle: LocalFeatureToggle): Boolean =
        when (featuresStateRepository.getState(toggle.key)) {
            FeatureEnabledState.DEFAULT -> featuresDefaults.getLocalDefault(toggle)
            FeatureEnabledState.ENABLED -> true
            FeatureEnabledState.DISABLED -> false
        }

    override fun isRemoteEnabled(toggle: RemoteFeatureToggle): Boolean =
        when (featuresStateRepository.getState(toggle.key)) {
            FeatureEnabledState.ENABLED -> true
            FeatureEnabledState.DISABLED -> false
            FeatureEnabledState.DEFAULT -> remoteFeaturesRepository.isEnabled(toggle.key)
        }
}