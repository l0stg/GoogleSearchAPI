package com.mintrocket.data.feature_toggling.repository.state

class ReleaseFeaturesStateRepositoryImpl : FeaturesStateRepository {

    override fun getState(key: String): FeatureEnabledState = FeatureEnabledState.DEFAULT

    override fun setState(key: String, state: FeatureEnabledState) {
        // No op
    }

}