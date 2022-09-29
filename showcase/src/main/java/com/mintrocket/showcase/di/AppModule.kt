package com.mintrocket.showcase.di

import com.mintrocket.modules.auth.core_ui.external.AuthErrorHandler
import com.mintrocket.modules.auth.core_ui.external.IAuthCodeDataSource
import com.mintrocket.modules.auth.core_ui.external.IAuthLoginPassDataSource
import com.mintrocket.modules.auth.core_ui.external.IAuthRouter
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.showcase.screens.authentication.AuthCodeDataSource
import com.mintrocket.showcase.screens.authentication.AuthErrorHandlerImpl
import com.mintrocket.showcase.screens.authentication.AuthLoginPassDataSource
import com.mintrocket.showcase.screens.authentication.AuthRouter
import com.squareup.moshi.Moshi
import org.koin.dsl.module

val appModule = module {
    single { ApplicationNavigator() }
    single {
        Moshi.Builder()
            .build()
    }
}

val commonAuthModule = module {
    single<IAuthCodeDataSource> { AuthCodeDataSource() }
    single<IAuthLoginPassDataSource> { AuthLoginPassDataSource() }
    single<IAuthRouter> { AuthRouter(get()) }
    single<AuthErrorHandler> { AuthErrorHandlerImpl(get()) }
}