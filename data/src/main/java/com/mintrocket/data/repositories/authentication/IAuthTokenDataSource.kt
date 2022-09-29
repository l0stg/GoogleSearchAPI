package com.mintrocket.data.repositories.authentication

interface IAuthTokenDataSource {

    val authToken: String?
    val tokenType: String?
    val refreshToken: String?
    val tokenExpireTime: Long?

    fun updateToken(
        newToken: String,
        newTokenType: String,
        newTokenExpiresIn: Long,
        newRefreshToken: String
    )

    fun clear()
}