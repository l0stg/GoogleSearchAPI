package com.mintrocket.data.model.network.auth

import com.squareup.moshi.Json

data class AuthUser(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    @field:Json(name = "is_notifiable")
    val notificationsEnabled: Boolean?,
    @field:Json(name = "is_subscribed")
    val hasPremium: Int
)
