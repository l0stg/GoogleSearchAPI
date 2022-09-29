package com.mintrocket.data.model.network.auth

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CheckCodeRequest(
    val phone: String,
    val code: String
)