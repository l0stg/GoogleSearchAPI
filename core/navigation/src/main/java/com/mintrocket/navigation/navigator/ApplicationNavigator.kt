package com.mintrocket.navigation.navigator

import com.mintrocket.navigation.screens.BaseAppScreen

open class ApplicationNavigator : BaseNavigator() {
    private val scopedNavigators = mutableMapOf<String, ScopedNavigator>()

    override fun setRootScreen(screen: BaseAppScreen) {
        // stack cleared and navigators are outdated now
        scopedNavigators.clear()
        super.setRootScreen(screen)
    }

    override fun setRootChain(screens: Array<BaseAppScreen>) {
        // stack cleared and navigators are outdated now
        scopedNavigators.clear()
        super.setRootChain(screens)
    }

    override fun finish() {
        // stack cleared and navigators are outdated now
        scopedNavigators.clear()
        super.finish()
    }

    fun getScopedNavigator(scopeName: String): ScopedNavigator {
        return scopedNavigators.getOrPut(scopeName) {
            ScopedNavigator(this, scopeName).apply {
                defaultFragmentAnimation = this@ApplicationNavigator.defaultFragmentAnimation
            }
        }
    }
}