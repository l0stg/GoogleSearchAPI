package com.mintrocket.data.di

fun getDataModules() = productionModulesList

private val productionModulesList = listOf(
    repositoriesModule,
    httpModule,
    dbModule,
    firebaseModule,
    preferencesModule,
    analyticsModule
)