package com.mintrocket.data.di

import com.mintrocket.data.analytics.features.*
import com.mintrocket.data.analytics.sender.*
import com.mintrocket.datacore.AppBuildConfig
import org.koin.dsl.module

val analyticsModule = module {

    single { LoggingAnalyticsSender() }

    single<AnalyticsSender> {
        get<LoggingAnalyticsSender>()
    }

    single { ExampleAnalytics(get()) }
}