package com.mintrocket.data.feature_toggling.repository.state

interface FeaturesStateRepository {
    fun getState(key: String): FeatureEnabledState
    fun setState(key: String, state: FeatureEnabledState)
}