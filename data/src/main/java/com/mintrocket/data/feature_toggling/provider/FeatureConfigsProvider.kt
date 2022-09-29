package com.mintrocket.data.feature_toggling.provider

import com.mintrocket.data.feature_toggling.features.toggles.LocalFeatureToggle
import com.mintrocket.data.feature_toggling.features.toggles.RemoteFeatureToggle

interface FeatureConfigsProvider {
    fun isLocalEnabled(toggle: LocalFeatureToggle): Boolean
    fun isRemoteEnabled(toggle: RemoteFeatureToggle): Boolean
}