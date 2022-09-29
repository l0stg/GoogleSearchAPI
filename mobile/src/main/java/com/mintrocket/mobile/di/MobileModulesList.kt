package com.mintrocket.mobile.di

import com.mintrocket.mobile.feature.auth.authModule
import com.mintrocket.modules.auth.core_ui.di.authFeatureModule

fun getMobileModules() = listOf(
    screensModule,
    viewModelsModule,

    authFeatureModule,
    authModule,
)