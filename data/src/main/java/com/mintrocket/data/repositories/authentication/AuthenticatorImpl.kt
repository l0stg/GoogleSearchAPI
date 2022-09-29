package com.mintrocket.data.repositories.authentication

import com.mintrocket.data.api.ApplicationApi
import com.mintrocket.datacore.extensions.attachAuthToken
import com.mintrocket.datacore.extensions.responseCount
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import timber.log.Timber

class AuthenticatorImpl(
    private val tokenDataSource: IAuthTokenDataSource,
    private val api: ApplicationApi,
    private val appHost: String
) : Authenticator {

    private val mutex = Mutex()

    override fun authenticate(
        route: Route?,
        response: Response
    ): Request? {
        return runBlocking {
            when {
                response.responseCount() > 1 -> null
                route?.address?.url?.host != appHost -> null
                else -> {
                    mutex.withLock {
                        var result: Request? = null

                        try {
                            val refreshToken =
                                tokenDataSource.refreshToken ?: return@runBlocking null
                            val newToken = api.refreshToken(refreshToken)
                            with(newToken) {
                                tokenDataSource.updateToken(
                                    accessToken,
                                    tokenType,
                                    expiresIn,
                                    refreshToken
                                )
                                val tokenType = tokenType
                                result = response.request
                                    .attachAuthToken(accessToken, tokenType)
                            }
                        } catch (e: Exception) {
                            Timber.e(e)
                        }

                        result
                    }
                }
            }
        }
    }
}