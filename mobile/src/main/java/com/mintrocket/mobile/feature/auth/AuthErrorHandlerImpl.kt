package com.mintrocket.mobile.feature.auth

import com.mintrocket.modules.auth.core_ui.external.AuthErrorHandler
import com.mintrocket.navigation.navigator.ApplicationNavigator

class AuthErrorHandlerImpl(
    private val navigator: ApplicationNavigator
) : AuthErrorHandler {

    override fun handleError(ex: Throwable) {
        navigator.handleErrorOnMain(ex)
    }
}