package com.mintrocket.mobile.feature.auth

import com.mintrocket.data.repositories.authentication.IAuthRepository
import com.mintrocket.modules.auth.core_ui.external.IAuthCodeDataSource

class AuthCodeDataSource(
    private val authRepository: IAuthRepository
) : IAuthCodeDataSource {

    override suspend fun sendCode(phone: String) = authRepository.sendCode(phone)

    override suspend fun checkCode(phone: String, code: String) =
        authRepository.checkCode(phone, code)
}