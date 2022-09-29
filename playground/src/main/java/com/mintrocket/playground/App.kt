package com.mintrocket.playground

import android.app.Application
import com.mintrocket.data.di.getDataModules
import com.mintrocket.mobile.di.getMobileModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                getMobileModules() + getDataModules()
            )
        }
    }
}