package com.mintrocket.security.example

import android.app.Application
import com.mintrocket.modules.security.core_ui.AppSecurity
import com.mintrocket.modules.security.core_ui.di.securityUiModule
import com.mintrocket.security.example.di.appModule
import com.mintrocket.security.example.di.security.securityFeatureModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class ExampleApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@ExampleApp)
            modules(
                appModule,
                securityUiModule,
                securityFeatureModule
            )
        }

        AppSecurity.init(this)
    }
}