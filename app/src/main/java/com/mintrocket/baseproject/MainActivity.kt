package com.mintrocket.baseproject

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mintrocket.baseproject.debug_screen.DebugScreenHelper
import com.mintrocket.mobile.screens.root.RootActivity
import org.koin.android.ext.android.inject

class MainActivity : RootActivity() {

    private val debugScreenHelper by inject<DebugScreenHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        debugScreenHelper.showDebugNotificationIfRequired(this)
    }
}