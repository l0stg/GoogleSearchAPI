package com.mintrocket.mobile.common

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.SharedPreferences
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.mintrocket.data.feature_toggling.features.LocalFeatures
import com.mintrocket.mobile.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import timber.log.Timber

class InAppUpdateHelper(
    private val activity: Activity
) : LifecycleObserver, KoinComponent {

    companion object {
        const val APP_UPDATE_REQUEST_CODE = 1991
        private const val LAST_APP_UPDATE_REQUEST_KEY = "app_update_request"
        private const val PREF_NAME = "in_app_update_pref"
        private const val UPDATE_INTERVAL_IN_MILLS = 604800000L
    }

    init {
        (activity as LifecycleOwner).lifecycle.addObserver(this)
    }

    private val preferences: SharedPreferences by lazy {
        activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    private val localFeatures by inject<LocalFeatures>()

    private var lastAppUpdateTime: Long
        get() = preferences.getLong(LAST_APP_UPDATE_REQUEST_KEY, 0L)
        set(value) {
            preferences.edit().putLong(LAST_APP_UPDATE_REQUEST_KEY, value).apply()
        }

    private val appUpdateManager: AppUpdateManager by lazy { AppUpdateManagerFactory.create(activity) }
    private val appUpdatedListener: InstallStateUpdatedListener by lazy {
        InstallStateUpdatedListener { installState: InstallState ->
            when {
                installState.installStatus() == InstallStatus.DOWNLOADED -> successLoading()
                installState.installStatus() == InstallStatus.INSTALLED ->
                    appUpdateManager.unregisterListener(appUpdatedListener)
                else -> Timber.d(
                    "InstallStateUpdatedListener: state: %s",
                    installState.installStatus()
                )
            }

        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        if (localFeatures.isInAppUpdateEnabled())
            checkForAppUpdate()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        if (localFeatures.isInAppUpdateEnabled())
            onResumeAppUpdate()
    }

    fun onActivityResult(resultCode: Int) {
        if (resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {
            Toast.makeText(
                activity,
                R.string.in_app_update_error,
                Toast.LENGTH_SHORT
            )
                .show()
            lastAppUpdateTime = 0L
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            lastAppUpdateTime = System.currentTimeMillis()
        }
    }

    private fun checkForAppUpdate() {
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            val installType = if (localFeatures.isInAppUpdateInstallImmediateEnabled()) {
                AppUpdateType.IMMEDIATE
            } else {
                AppUpdateType.FLEXIBLE
            }

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(installType) && checkUpdateIntervalPassed()
            ) {

                // Request the update.
                try {
                    appUpdateManager.registerListener(
                        appUpdatedListener
                    )

                    appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        installType,
                        activity,
                        APP_UPDATE_REQUEST_CODE
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun onResumeAppUpdate() {
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                val installType = if (localFeatures.isInAppUpdateInstallImmediateEnabled()) {
                    AppUpdateType.IMMEDIATE // If the update is downloaded but not installed,
                } else {
                    AppUpdateType.FLEXIBLE // notify the user to complete the update.
                }

                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    successLoading()
                }

                //Check if Immediate update is required
                try {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            installType,
                            activity,
                            APP_UPDATE_REQUEST_CODE
                        )
                    }
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
    }

    private fun checkUpdateIntervalPassed(): Boolean {
        val lastUpdateDate = lastAppUpdateTime
        if (lastUpdateDate == 0L) return true

        val currentTime = System.currentTimeMillis()
        return (currentTime - lastUpdateDate) > UPDATE_INTERVAL_IN_MILLS
    }

    private fun successLoading() {
        val dialog = AlertDialog.Builder(activity).apply {
            setMessage(R.string.in_app_update_success)
            setPositiveButton(R.string.in_app_update_restart)
            { d, _ ->
                appUpdateManager.completeUpdate()
                d.dismiss()
            }
            setNegativeButton(R.string.in_app_update_cancel) { d, _ ->
                d.dismiss()
            }
        }
        dialog.show()
    }
}