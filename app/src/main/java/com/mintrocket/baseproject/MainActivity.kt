package com.mintrocket.baseproject

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mintrocket.baseproject.debug_screen.DebugScreenHelper
import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.mobile.screens.root.RootActivity
import com.mintrocket.navigation.screens.BaseAppScreen
import org.koin.android.ext.android.inject

class MainActivity : RootActivity() {

    private val debugScreenHelper by inject<DebugScreenHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        debugScreenHelper.showDebugNotificationIfRequired(this)
    }

    override fun showSnackBar(
        text: TextContainer,
        actionText: TextContainer?,
        action: (() -> Unit)?,
        dismissAction: (() -> Unit)?
    ) {
        TODO("Not yet implemented")
    }


    override fun handleError(throwable: Throwable, requiredAuthRedirect: Boolean): BaseAppScreen? {
        TODO("Not yet implemented")
    }
}