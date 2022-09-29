package com.mintrocket.security.example.di

import com.mintrocket.navigation.ScreenResultsBuffer
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.security.example.screens.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { ApplicationNavigator() }
    single { ScreenResultsBuffer() }

    viewModel { MainViewModel(get(), get()) }
}