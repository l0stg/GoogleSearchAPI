package com.mintrocket.showcase.screens.authentication

import com.mintrocket.datacore.errorhandling.WrongCredentialsException
import com.mintrocket.modules.auth.core_ui.external.IAuthLoginPassDataSource

class AuthLoginPassDataSource : IAuthLoginPassDataSource {

    override suspend fun auth(login: String, password: String) {
        throw WrongCredentialsException(RuntimeException())
    }
}