package com.mintrocket.data.di

import android.content.Context
import android.content.SharedPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val SETTINGS_PREFS = named("SETTINGS_PREFS")
val AUTH_DATA_PREFS = named("AUTH_DATA_PREFS")
val ID_GENERATOR_PREFS = named("ID_GENERATOR_PREFS")
val DEBUG_FEATURE_CONFIG_PREFS = named("DEBUG_FEATURE_CONFIG_PREFS")

val preferencesModule = module {

    single<SharedPreferences>(DEBUG_FEATURE_CONFIG_PREFS) {
        androidContext()
            .applicationContext
            .getSharedPreferences("debug_feature_configs_v2", Context.MODE_PRIVATE)
    }

    single<SharedPreferences>(ID_GENERATOR_PREFS) {
        androidContext()
            .applicationContext
            .getSharedPreferences("NotificationIdGeneratorPreferences", Context.MODE_PRIVATE)
    }

    single<SharedPreferences>(AUTH_DATA_PREFS) {
        androidContext()
            .applicationContext
            .getSharedPreferences("auth_data", Context.MODE_PRIVATE)
    }

    single<SharedPreferences>(SETTINGS_PREFS) {
        androidContext()
            .applicationContext
            .getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    single {
        get<SharedPreferences>(SETTINGS_PREFS)
    } bind (SharedPreferences::class)
}