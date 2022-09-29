package com.mintrocket.baseproject

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.mintrocket.baseproject.di.appModule
import com.mintrocket.baseproject.di.featureTogglingModule
import com.mintrocket.data.di.getDataModules
import com.mintrocket.mobile.di.getMobileModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()


        if (isMainProcess()) {
            startKoin {
                androidContext(this@App)
                modules(
                    listOf(appModule, featureTogglingModule)
                            + getMobileModules()
                            + getDataModules()
                )
            }

            if (BuildConfig.DEBUG) {
                //todo add fabric tree
                Timber.plant(Timber.DebugTree())
            }
        }
    }

    private fun isMainProcess(): Boolean {
        return packageName == getProcessNameLegacy()
    }

    private fun getProcessNameLegacy(): String? {
        val mypid = Process.myPid()
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager
        manager?.runningAppProcesses?.forEach {
            if (it.pid == mypid) return it.processName
        }
        // may never return null
        return null
    }
}