package com.mintrocket.security.example.screens

import android.content.Context
import android.content.Intent
import com.mintrocket.modules.security.core_ui.screens.barrier.PinBarrierActivity
import com.mintrocket.modules.security.core_ui.screens.check.PinCheckFragment
import com.mintrocket.modules.security.core_ui.screens.create.PinCreateFragment
import com.mintrocket.modules.security.core_ui.screens.fingerprint.FingerprintFragment
import com.mintrocket.modules.security.core_ui.screens.settings.SecuritySettingsFragment
import com.mintrocket.navigation.screens.ActivityScreen
import com.mintrocket.navigation.screens.FragmentScreen
import com.mintrocket.security.example.screens.home.HomeFragment
import com.mintrocket.security.example.screens.main.MainActivity
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainScreen(val newTask: Boolean = false) : SupportAppScreen() {
    override fun getActivityIntent(context: Context): Intent =
        MainActivity.getIntent(context, newTask)
}

class HomeScreen : FragmentScreen() {
    override fun createFragment() = HomeFragment()
}

class FingerprintScreen : FragmentScreen() {
    override fun createFragment() = FingerprintFragment.newInstance()
}

class PinBarrierScreen : ActivityScreen() {
    override fun createIntent(context: Context) = PinBarrierActivity.getIntent(context)
}

class PinCheckScreen : FragmentScreen() {
    override fun createFragment() = PinCheckFragment.newInstance()
}

class PinCreateScreen : FragmentScreen() {
    override fun createFragment() = PinCreateFragment.newInstance()
}

class SecuritySettingsScreen : FragmentScreen() {
    override fun createFragment() = SecuritySettingsFragment.newInstance()
}

