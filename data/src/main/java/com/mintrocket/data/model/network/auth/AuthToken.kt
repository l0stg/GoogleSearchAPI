package com.mintrocket.data.model.network.auth

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String,
    val expiresIn: Long
)