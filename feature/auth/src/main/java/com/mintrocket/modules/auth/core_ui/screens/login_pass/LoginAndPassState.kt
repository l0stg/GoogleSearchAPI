package com.mintrocket.modules.auth.core_ui.screens.login_pass

sealed class LoginAndPassState {
    object Empty : LoginAndPassState()
    data class WrongCredentials(
        val errorMessage: Int
    ) : LoginAndPassState()

    data class InvalidLoginFormat(
        val errorMessage: Int
    ) : LoginAndPassState()
}