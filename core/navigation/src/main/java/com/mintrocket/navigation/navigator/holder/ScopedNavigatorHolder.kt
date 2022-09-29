package com.mintrocket.navigation.navigator.holder

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.mintrocket.navigation.NavigatorContainer
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ScopedNavigatorHolder(
    activity: FragmentActivity,
    fragmentManager: FragmentManager,
    navContainer: NavigatorContainer,
    containerId: Int,
    val scopeName: String
) : AppNavigatorHolder(activity, fragmentManager, navContainer, containerId) {

    companion object {
        const val ARG_SCOPE_NAME = "ScopedNavigator:scope_name"
    }

    override fun createFragment(screen: SupportAppScreen?): Fragment {
        return super.createFragment(screen).apply {
            arguments = (arguments ?: Bundle()).apply {
                putString(ARG_SCOPE_NAME, scopeName)
            }
        }
    }
}