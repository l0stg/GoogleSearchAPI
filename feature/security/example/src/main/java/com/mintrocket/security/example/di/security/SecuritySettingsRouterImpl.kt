package com.mintrocket.security.example.di.security

import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.datacore.utils.toContainer
import com.mintrocket.modules.security.external.SecuritySettingsRouter
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.security.example.R
import com.mintrocket.security.example.screens.PinCheckScreen
import com.mintrocket.security.example.screens.PinCreateScreen

class SecuritySettingsRouterImpl(
    private val navigator: ApplicationNavigator
) : SecuritySettingsRouter {

    override fun exit() {
        navigator.popScreen()
    }

    override fun openCreate() {
        navigator.navigateTo(PinCreateScreen())
    }

    override fun openCheck() {
        navigator.navigateTo(PinCheckScreen())
    }

    override fun showMsg(msg: String) {
        navigator.showToast(msg.toContainer())
    }

    override fun showSuggestToBiometricDialog(action: () -> Unit) {
        navigator.showDialog(
            message = TextContainer.ResContainer(R.string.security_settings_suggest_msg),
            positiveButtonText = TextContainer.ResContainer(R.string.security_settings_suggest_positive),
            positiveButtonAction = { _, _ -> action.invoke() },
            negativeButtonText = TextContainer.ResContainer(android.R.string.cancel)
        )
    }
}