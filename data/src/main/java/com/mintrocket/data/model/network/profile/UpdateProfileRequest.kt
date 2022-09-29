package com.mintrocket.data.model.network.profile

data class UpdateProfileRequest(
    val name: String,
    val surname: String,
    val email: String
)