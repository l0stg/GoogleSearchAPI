package com.mintrocket.baseproject.di

import com.mintrocket.data.di.DEBUG_FEATURE_CONFIG_PREFS
import com.mintrocket.data.feature_toggling.FeatureToggleUpdateUseCase
import com.mintrocket.data.feature_toggling.features.FeaturesDefaults
import com.mintrocket.data.feature_toggling.features.LocalFeatures
import com.mintrocket.data.feature_toggling.features.RemoteFeatures
import com.mintrocket.data.feature_toggling.provider.FeatureConfigsProvider
import com.mintrocket.data.feature_toggling.provider.FeatureConfigsProviderImpl
import com.mintrocket.data.feature_toggling.repository.state.DebugFeaturesStateRepositoryImpl
import com.mintrocket.data.feature_toggling.repository.state.ReleaseFeaturesStateRepositoryImpl
import com.mintrocket.datacore.AppBuildConfig
import org.koin.dsl.module

val featureTogglingModule = module {

    single { FeaturesDefaults() }

    single {
        val appBuildConfig = get<AppBuildConfig>()
        if (appBuildConfig.isDebugScreenEnabled) {
            DebugFeaturesStateRepositoryImpl(get(DEBUG_FEATURE_CONFIG_PREFS))
        } else {
            ReleaseFeaturesStateRepositoryImpl()
        }
    }

    single<FeatureConfigsProvider> { FeatureConfigsProviderImpl(get(), get(), get()) }

    single {
        FeatureToggleUpdateUseCase(
            remoteFeaturesRepository = get(),
            remoteConfigEnabled = {
                return@FeatureToggleUpdateUseCase get<LocalFeatures>().isRemoteConfigEnabled()
            }
        )
    }

    single { LocalFeatures(get()) }
    single { RemoteFeatures(get()) }

}