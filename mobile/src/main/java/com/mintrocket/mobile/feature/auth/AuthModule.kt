package com.mintrocket.mobile.feature.auth

import com.mintrocket.mobile.R
import com.mintrocket.modules.auth.core_ui.AuthFeatureConfig
import com.mintrocket.modules.auth.core_ui.AuthLegal
import com.mintrocket.modules.auth.core_ui.AuthTypeConfig
import com.mintrocket.modules.auth.core_ui.external.AuthErrorHandler
import com.mintrocket.modules.auth.core_ui.external.IAuthCodeDataSource
import com.mintrocket.modules.auth.core_ui.external.IAuthRouter
import org.koin.dsl.module

val authModule = module {

    single {
        AuthFeatureConfig(
            authTypeConfig = AuthTypeConfig.PhoneAndCode(
                codeInMessageRegex = Regex("([\\d]{4})\$"),
                codeLength = 5,
                confirmCodeWithButton = false
            ),


            legalTextRes = R.string.auth_agreements_format,
            legals = listOf(
                AuthLegal("data processing", R.string.auth_data_processing),
                AuthLegal("privacy policy", R.string.auth_privacy_policy)
            ),
        )
    }


    single<IAuthCodeDataSource> { AuthCodeDataSource(get()) }
    single<IAuthRouter> { AuthRouter(get()) }
    single<AuthErrorHandler> { AuthErrorHandlerImpl(get()) }
}
