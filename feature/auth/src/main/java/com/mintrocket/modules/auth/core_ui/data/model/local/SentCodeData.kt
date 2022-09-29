package com.mintrocket.modules.auth.core_ui.data.model.local

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SentCodeData(
    val phone: String,
    val sentAt: Long
)