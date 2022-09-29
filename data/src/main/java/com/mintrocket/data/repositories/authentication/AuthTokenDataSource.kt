package com.mintrocket.data.repositories.authentication

import android.content.SharedPreferences

class AuthTokenDataSource(
    private val sharedPreferences: SharedPreferences
) : IAuthTokenDataSource {

    companion object {
        private const val TOKEN = "token"
        private const val TOKEN_TYPE = "token_type"
        private const val TOKEN_EXPIRE_TIME = "token_expire_in"
        private const val REFRESH_TOKEN = "refresh_token"
        private const val THOUSAND = 1000L
    }

    override val authToken: String?
        get() = sharedPreferences.getString(TOKEN, null)

    override val tokenType: String?
        get() = sharedPreferences.getString(TOKEN_TYPE, null)

    override val tokenExpireTime: Long?
        get() = sharedPreferences.getLong(TOKEN_EXPIRE_TIME, 0L)

    override val refreshToken: String?
        get() = sharedPreferences.getString(REFRESH_TOKEN, null)

    override fun updateToken(
        newToken: String,
        newTokenType: String,
        newTokenExpiresIn: Long,
        newRefreshToken: String
    ) {
        val exTime = newTokenExpiresIn * THOUSAND + System.currentTimeMillis()
        sharedPreferences.edit()
            .putString(TOKEN, newToken)
            .putString(REFRESH_TOKEN, newRefreshToken)
            .putString(TOKEN_TYPE, newTokenType)
            .putLong(
                TOKEN_EXPIRE_TIME,
                exTime
            )
            .apply()
    }

    override fun clear() {
        sharedPreferences.edit()
            .remove(TOKEN)
            .remove(REFRESH_TOKEN)
            .remove(TOKEN_TYPE)
            .remove(TOKEN_EXPIRE_TIME)
            .apply()
    }
}