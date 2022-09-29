package com.mintrocket.baseproject.debug_screen.environment.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EnvironmentEntry(
    val id: String,
    val name: String,
    val url: String,
    val editable: Boolean
) : Parcelable
