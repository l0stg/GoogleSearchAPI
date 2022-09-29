package com.mintrocket.data.repositories.authentication

import com.mintrocket.datacore.extensions.attachAuthToken
import com.mintrocket.datacore.extensions.proceedForHost
import okhttp3.Interceptor
import okhttp3.Response

class AuthTokenInterceptor(
    private val tokenDataSource: IAuthTokenDataSource,
    private val appHost: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response =
        chain.proceedForHost(appHost) {
            val token = tokenDataSource.authToken

            if (token == null) {
                chain.request()
            } else {
                val tokenType = requireNotNull(tokenDataSource.tokenType) {
                    "Token exists but token type is null"
                }
                chain.request().attachAuthToken(token, tokenType)
            }
        }
}