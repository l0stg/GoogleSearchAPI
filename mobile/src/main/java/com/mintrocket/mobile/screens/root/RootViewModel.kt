package com.mintrocket.mobile.screens.root

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.data.repositories.authentication.IAuthRepository
import com.mintrocket.mobile.navigation.PagerScreen
import com.mintrocket.modules.auth.core_ui.AuthFeature
import com.mintrocket.navigation.navigator.ApplicationNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class RootViewModel(
    private val navigator: ApplicationNavigator,
    private val authRepository: IAuthRepository,
    private val authFeature: AuthFeature,
) : ViewModel() {

    val progressLD = MutableLiveData<Boolean>()

    fun initNavigation() {
        viewModelScope.launch {
            val hasAuth = authRepository.isUserAuthenticated()
            if (hasAuth) {
                navigator.setRootScreen(PagerScreen())
            } else {
                authFeature.runAuthFlow()
            }
        }
    }

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authRepository.logout()
            }.onFailure {
                Timber.d(it)
            }
        }
    }
}