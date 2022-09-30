package com.mintrocket.baseproject.di

import com.mintrocket.baseproject.AppBuildConfigImpl
import com.mintrocket.baseproject.debug_screen.DebugScreenHelper
import com.mintrocket.baseproject.debug_screen.environment.EnvironmentAddViewModel
import com.mintrocket.baseproject.debug_screen.environment.EnvironmentViewModel
import com.mintrocket.baseproject.debug_screen.environment.data.DebugApiUrlProviderImpl
import com.mintrocket.baseproject.debug_screen.environment.data.EnvironmentRepository
import com.mintrocket.baseproject.debug_screen.logreader.LogReaderViewModel
import com.mintrocket.baseproject.debug_screen.logreader.data.LogReaderRepository
import com.mintrocket.data.api.ApiUrlProvider
import com.mintrocket.data.api.ApiUrlProviderImpl
import com.mintrocket.datacore.AppBuildConfig
import com.mintrocket.navigation.ScreenResultsBuffer
import com.mintrocket.navigation.navigator.ApplicationNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<AppBuildConfig> { AppBuildConfigImpl() }

    single { ApplicationNavigator() }
    single { ScreenResultsBuffer() }

    single {
        val appBuildConfig = get<AppBuildConfig>()
        DebugScreenHelper(appBuildConfig.isDebugScreenEnabled)
    }

    single<ApiUrlProvider> {
        val appBuildConfig = get<AppBuildConfig>()
        if (appBuildConfig.isDebugScreenEnabled) {
            DebugApiUrlProviderImpl(get())
        } else {
            ApiUrlProviderImpl(get())
        }
    }


    single { LogReaderRepository() }
    viewModel { LogReaderViewModel(get()) }

    single { EnvironmentRepository(get(), get(), get()) }
    viewModel { EnvironmentViewModel(get()) }
    viewModel { EnvironmentAddViewModel(get()) }

}