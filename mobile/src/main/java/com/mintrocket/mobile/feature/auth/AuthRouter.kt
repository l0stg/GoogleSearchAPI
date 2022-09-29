package com.mintrocket.mobile.feature.auth

import androidx.fragment.app.Fragment
import com.mintrocket.datacore.utils.toContainer
import com.mintrocket.mobile.navigation.PagerScreen
import com.mintrocket.modules.auth.core_ui.external.IAuthRouter
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.navigation.screens.FragmentScreen

class AuthRouter(
    private val navigator: ApplicationNavigator
) : IAuthRouter {

    override fun popScreen() {
        navigator.popScreen()
    }

    override fun openFragment(fabric: () -> Fragment) {
        navigator.navigateTo(
            object : FragmentScreen() {
                override fun createFragment() = fabric.invoke()
            }
        )
    }

    override fun openFragmentAsHome(fabric: () -> Fragment) {
        navigator.setRootScreen(
            object : FragmentScreen() {
                override fun createFragment() = fabric.invoke()
            }
        )
    }

    override fun openOnSuccess() {
        navigator.setRootScreen(PagerScreen())
    }

    override fun openLegalDoc(legalId: String) {
        navigator.showToast("openLegal $legalId".toContainer())
    }
}