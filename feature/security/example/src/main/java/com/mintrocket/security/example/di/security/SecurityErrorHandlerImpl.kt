package com.mintrocket.security.example.di.security

import com.mintrocket.modules.security.external.SecurityErrorHandler
import com.mintrocket.navigation.navigator.ApplicationNavigator

class SecurityErrorHandlerImpl(
    private val navigator: ApplicationNavigator
) : SecurityErrorHandler {

    override fun handleError(ex: Throwable) {
        navigator.handleErrorOnMain(ex)
    }
}