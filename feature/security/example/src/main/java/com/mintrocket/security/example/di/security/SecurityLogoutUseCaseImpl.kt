package com.mintrocket.security.example.di.security

import com.mintrocket.modules.security.data.SecurityRepository
import com.mintrocket.modules.security.external.SecurityLogoutUseCase

class SecurityLogoutUseCaseImpl(
    private val securityRepository: SecurityRepository
) : SecurityLogoutUseCase {

    override suspend fun logout() {
        securityRepository.clear()
    }
}