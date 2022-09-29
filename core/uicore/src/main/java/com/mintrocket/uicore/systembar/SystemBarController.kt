package com.mintrocket.uicore.systembar

import android.app.Activity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

class SystemBarController : DefaultLifecycleObserver {

    private var activity: Activity? = null

    /*
    * Nullable потому, что могут использоваться несколько экземпляров одновременно и каждый
    * должен менять только то, что нужно.
    * */
    var targetStatusBarColor: Int? = null
        private set
    var targetNavBarColor: Int? = null
        private set
    var targetStatusBarIsLight: Boolean? = null
        private set
    var targetNavBarIsLight: Boolean? = null
        private set

    fun attach(lifecycle: Lifecycle, activity: Activity): SystemBarController {
        this.activity = activity
        lifecycle.addObserver(this)
        return this
    }

    fun update(
        statusBarColor: Int? = targetStatusBarColor,
        navBarColor: Int? = targetNavBarColor,
        statusBarIsLight: Boolean? = targetStatusBarIsLight,
        navBarIsLight: Boolean? = targetNavBarIsLight
    ): SystemBarController {
        targetStatusBarColor = statusBarColor
        targetNavBarColor = navBarColor
        targetStatusBarIsLight = statusBarIsLight
        targetNavBarIsLight = navBarIsLight
        updateStatusBarColor()
        return this
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        updateStatusBarColor()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        restoreDefault()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        this.activity = null
    }

    private fun restoreDefault() {
        // Восстанавливаем только те, которые были изменены
        activity?.also { activity ->
            if (targetStatusBarColor != null) {
                activity.setStatusBarColor(activity.getThemeStatusBarColor())
            }
            if (targetNavBarColor != null) {
                activity.setNavigationBarColor(activity.getThemeNavigationBarColor())
            }
            if (targetStatusBarIsLight != null) {
                activity.setLightStatusBar(activity.getThemeLightStatusBar())
            }
            if (targetNavBarIsLight != null) {
                activity.setLightNavigationBar(activity.getThemeLightNavigationBar())
            }
        }
    }

    private fun updateStatusBarColor(
        statusBarColor: Int? = targetStatusBarColor,
        navBarColor: Int? = targetNavBarColor,
        statusBarIsLight: Boolean? = targetStatusBarIsLight,
        navBarIsLight: Boolean? = targetNavBarIsLight
    ) {
        // Устанавливаем только не null и не такие-же
        activity?.also { activity ->
            if (statusBarColor != null && statusBarColor != activity.getStatusBarColor()) {
                activity.setStatusBarColor(statusBarColor)
            }
            if (navBarColor != null && navBarColor != activity.getNavigationBarColor()) {
                activity.setNavigationBarColor(navBarColor)
            }
            if (statusBarIsLight != null && statusBarIsLight != activity.getLightStatusBar()) {
                activity.setLightStatusBar(statusBarIsLight)
            }
            if (navBarIsLight != null && navBarIsLight != activity.getLightNavigationBar()) {
                activity.setLightNavigationBar(navBarIsLight)
            }
        }
    }
}