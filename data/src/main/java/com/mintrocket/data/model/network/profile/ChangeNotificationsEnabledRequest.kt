package com.mintrocket.data.model.network.profile

import com.squareup.moshi.Json

data class ChangeNotificationsEnabledRequest(
    @field:Json(name = "is_notifiable")
    val enabled: Boolean
)