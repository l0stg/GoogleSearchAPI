package com.mintrocket.data.feature_toggling.features

import com.mintrocket.data.feature_toggling.features.toggles.LocalFeatureToggle
import com.mintrocket.data.feature_toggling.provider.FeatureConfigsProvider

class LocalFeatures(
    private val featureConfigsProvider: FeatureConfigsProvider
) {

    fun isRemoteConfigEnabled(): Boolean =
        featureConfigsProvider.isLocalEnabled(LocalFeatureToggle.REMOTE_CONFIG)

    fun isInAppUpdateEnabled(): Boolean =
        featureConfigsProvider.isLocalEnabled(LocalFeatureToggle.IN_APP_UPDATE)

    fun isInAppUpdateInstallImmediateEnabled(): Boolean =
        featureConfigsProvider.isLocalEnabled(LocalFeatureToggle.IN_APP_UPDATE_INSTALL_IMMEDIATE)

    fun isImageOneEnabled(): Boolean =
        featureConfigsProvider.isLocalEnabled(LocalFeatureToggle.IMAGE_ONE)

    fun isShowButtonEnabled(): Boolean =
        featureConfigsProvider.isLocalEnabled(LocalFeatureToggle.SHOW_BUTTON)
}