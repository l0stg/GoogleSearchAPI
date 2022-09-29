package com.mintrocket.data.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.dsl.module

val firebaseModule = module {
    single { FirebaseRemoteConfig.getInstance() }
}