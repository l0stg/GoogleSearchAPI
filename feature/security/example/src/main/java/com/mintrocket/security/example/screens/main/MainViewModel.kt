package com.mintrocket.security.example.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.modules.security.launch.SecurityLaunchController
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.security.example.screens.HomeScreen
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class MainViewModel(
    private val launchController: SecurityLaunchController,
    private val navigator: ApplicationNavigator
) : ViewModel() {

    fun initNavigation() {
        // Здесь может быть более сложная логика запуска экрана, например с обработкой deeplink
        launchController
            .observeAccess()
            .filter { !launchController.isAppStarted() }
            .onEach {
                navigator.setRootScreen(HomeScreen())
                launchController.onAppStarted()
            }
            .launchIn(viewModelScope)

        launchController.requireCheck()
    }
}