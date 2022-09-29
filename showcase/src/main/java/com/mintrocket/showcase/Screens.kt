package com.mintrocket.showcase

import com.mintrocket.navigation.screens.FragmentScreen
import com.mintrocket.showcase.screens.authentication.AuthenticationLaunchFragment
import com.mintrocket.showcase.screens.FeaturesListFragment

class FeaturesScreen : FragmentScreen() {
    override fun createFragment() = FeaturesListFragment.newInstance()
}

class AuthenticationLaunchScreen : FragmentScreen() {
    override fun createFragment() = AuthenticationLaunchFragment.newInstance()
}