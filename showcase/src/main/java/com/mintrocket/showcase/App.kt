package com.mintrocket.showcase

import android.app.Application
import com.mintrocket.modules.auth.core_ui.di.authFeatureModule
import com.mintrocket.showcase.di.appModule
import com.mintrocket.showcase.di.commonAuthModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                appModule, authFeatureModule, commonAuthModule
            )
        }

        Timber.plant(Timber.DebugTree())
    }
}