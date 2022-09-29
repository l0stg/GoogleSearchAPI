package com.mintrocket.showcase.screens

import androidx.annotation.StringRes
import com.mintrocket.showcase.R

enum class Features(
    @StringRes val nameRes: Int
) {
    AUTHENTICATION(R.string.feature_authentication)
}