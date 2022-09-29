package com.mintrocket.security.example.di.security

import com.mintrocket.datacore.utils.toContainer
import com.mintrocket.modules.security.external.FingerprintRouter
import com.mintrocket.navigation.navigator.ApplicationNavigator

class FingerprintRouterImpl(
    private val navigator: ApplicationNavigator
) : FingerprintRouter {

    override fun exit() {
        navigator.popScreen()
    }

    override fun showMsg(msg: String) {
        navigator.showToast(msg.toContainer())
    }
}