package com.mintrocket.data.feature_toggling.features

import com.mintrocket.data.feature_toggling.features.toggles.RemoteFeatureToggle
import com.mintrocket.data.feature_toggling.provider.FeatureConfigsProvider

class RemoteFeatures(
    private val featureConfigsProvider: FeatureConfigsProvider
) {

    fun coreEnabled() =
        featureConfigsProvider.isRemoteEnabled(RemoteFeatureToggle.CORE_ENABLED)
}