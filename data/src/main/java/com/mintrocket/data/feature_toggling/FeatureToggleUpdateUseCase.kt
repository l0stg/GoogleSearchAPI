package com.mintrocket.data.feature_toggling

import com.mintrocket.data.feature_toggling.repository.remote.RemoteFeaturesRepository
import timber.log.Timber

class FeatureToggleUpdateUseCase(
    private val remoteFeaturesRepository: RemoteFeaturesRepository,
    private val remoteConfigEnabled: () -> Boolean
) {

    suspend fun updateFeatureToggles() {
        Timber.d("TESTING remote ${remoteConfigEnabled.invoke()}")
        if (remoteConfigEnabled.invoke()) {
            remoteFeaturesRepository.updateToggles()
        }
    }
}