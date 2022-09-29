package com.mintrocket.modules.security.core_ui

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.mintrocket.modules.security.launch.SecurityAppFinishListener
import com.mintrocket.modules.security.launch.SecurityLaunchController
import org.koin.android.ext.android.get

object AppSecurity {

    fun init(application: Application) {
        val launchController = application.get<SecurityLaunchController>()
        ProcessLifecycleOwner.get().lifecycle.addObserver(launchController)
        application.registerActivityLifecycleCallbacks(SecurityAppFinishListener(launchController))
    }
}