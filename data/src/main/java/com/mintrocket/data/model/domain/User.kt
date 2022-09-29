package com.mintrocket.data.model.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val hasPremium: Boolean,
    var notificationsEnabled: Boolean
) : Parcelable