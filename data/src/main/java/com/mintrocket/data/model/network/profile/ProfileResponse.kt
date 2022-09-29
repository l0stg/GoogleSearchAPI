package com.mintrocket.data.model.network.profile

import com.mintrocket.data.model.network.auth.AuthUser

data class ProfileResponse(
    val status: Boolean,
    val data: AuthUser
)