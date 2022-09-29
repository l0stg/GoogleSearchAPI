package com.mintrocket.data.feature_toggling.repository.state

import android.content.SharedPreferences

class DebugFeaturesStateRepositoryImpl(
    private val preferences: SharedPreferences
) : FeaturesStateRepository {

    override fun getState(key: String): FeatureEnabledState {
        val ordinal = preferences.getInt(key, FeatureEnabledState.DEFAULT.ordinal)
        return FeatureEnabledState.values().getOrNull(ordinal) ?: FeatureEnabledState.DEFAULT
    }

    override fun setState(key: String, state: FeatureEnabledState) {
        preferences.edit()
            .putInt(key, state.ordinal)
            .apply()
    }

}