package com.mintrocket.data.model.network.profile

import com.squareup.moshi.Json

data class ChangeSubscriptionRequest(
    @field:Json(name = "is_subscribed")
    val isSubscribed: Boolean
)