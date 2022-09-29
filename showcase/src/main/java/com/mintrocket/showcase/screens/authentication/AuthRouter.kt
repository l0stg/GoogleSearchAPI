package com.mintrocket.showcase.screens.authentication

import androidx.fragment.app.Fragment
import com.mintrocket.datacore.utils.toContainer
import com.mintrocket.modules.auth.core_ui.external.IAuthRouter
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.navigation.screens.FragmentScreen
import com.mintrocket.showcase.FeaturesScreen

class AuthRouter(private val navigator: ApplicationNavigator) : IAuthRouter {
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
        navigator.navigateTo(
            object : FragmentScreen() {
                override fun createFragment() = fabric.invoke()
            }
        )
    }

    override fun openOnSuccess() {
        navigator.setRootScreen(FeaturesScreen())
    }

    override fun openLegalDoc(legalId: String) {
        navigator.showToast("openLegal $legalId".toContainer())
    }
}