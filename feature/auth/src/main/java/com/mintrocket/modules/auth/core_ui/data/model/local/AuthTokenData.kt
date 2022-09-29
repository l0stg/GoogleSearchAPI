package com.mintrocket.modules.auth.core_ui.data.model.local

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthTokenData(
    val accessToken: String,
    val refreshToken: String? = null,
    val tokenType: String? = null,
    val expiresIn: Long? = null
)