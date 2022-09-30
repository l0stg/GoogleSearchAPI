package com.mintrocket.mobile.di

import com.mintrocket.modules.auth.core_ui.di.authFeatureModule

fun getMobileModules() = listOf(
    screensModule,
    viewModelsModule,
    authFeatureModule,
)