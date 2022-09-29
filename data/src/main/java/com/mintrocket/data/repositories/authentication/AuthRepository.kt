package com.mintrocket.data.repositories.authentication

import com.mintrocket.data.api.ApplicationApi
import com.mintrocket.data.model.network.auth.CheckCodeRequest
import com.mintrocket.datacore.extensions.awaitEmpty

internal class AuthRepository(
    private val api: ApplicationApi,
    private val tokenDataSource: IAuthTokenDataSource
) : IAuthRepository {

    override suspend fun sendCode(phone: String) {
        api.sendCode(phone).awaitEmpty()
    }

    override suspend fun checkCode(phone: String, code: String) {
        val token = api.checkCode(
            CheckCodeRequest(phone, code)
        )

        tokenDataSource.updateToken(
            token.accessToken,
            token.tokenType,
            token.expiresIn,
            token.refreshToken
        )
    }

    override suspend fun isUserAuthenticated(): Boolean {
        return tokenDataSource.authToken != null
    }

    override suspend fun logout() {
        tokenDataSource.clear()
    }
}