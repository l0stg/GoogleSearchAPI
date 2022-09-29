package com.mintrocket.security.example.di.security

import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.modules.security.external.PinCreateRouter
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.security.example.R
import com.mintrocket.security.example.screens.FingerprintScreen

class PinCreateRouterImpl(
    private val navigator: ApplicationNavigator
) : PinCreateRouter {

    override fun exit() {
        navigator.popScreen()
    }

    override fun replaceFingerprint() {
        navigator.replaceTo(FingerprintScreen())
    }

    override fun showWrongCode() {
        navigator.showToast(TextContainer.ResContainer(R.string.security_create_wrong_code))
    }
}