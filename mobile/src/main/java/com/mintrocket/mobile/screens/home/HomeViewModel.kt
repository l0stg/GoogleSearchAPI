package com.mintrocket.mobile.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.data.repositories.authentication.IAuthRepository
import com.mintrocket.modules.auth.core_ui.AuthFeature
import com.mintrocket.navigation.navigator.ApplicationNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val navigator: ApplicationNavigator,
    private val authRepository: IAuthRepository,
    private val authFeature: AuthFeature,
) : ViewModel() {

    fun onLogoutClick() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authRepository.logout()
            }
            authFeature.runAuthFlow()
        }
    }
}