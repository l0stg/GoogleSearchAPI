package com.mintrocket.modules.security.launch

import android.app.Activity
import android.app.Application
import android.os.Bundle

class SecurityAppFinishListener(
    private val launchController: SecurityLaunchController
) : Application.ActivityLifecycleCallbacks {

    /*
    * Определяем, что все activity завершаются.
    * Слушаем именно onPause, т.к. в onStop и т.д. может приходить с существенной задержкой (500ms+)
    * */
    override fun onActivityPaused(activity: Activity) {
        if (activity.isTaskRoot && activity.isFinishing) {
            launchController.onAppFinishing()
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Do nothing.
    }

    override fun onActivityStarted(activity: Activity) {
        // Do nothing.
    }

    override fun onActivityResumed(activity: Activity) {
        // Do nothing.
    }

    override fun onActivityStopped(activity: Activity) {
        // Do nothing.
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Do nothing.
    }

    override fun onActivityDestroyed(activity: Activity) {
        // Do nothing.
    }
}