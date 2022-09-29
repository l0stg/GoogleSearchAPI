package com.mintrocket.data.analytics.extensions

import com.mintrocket.data.analytics.AnalyticsParamNames
import java.util.concurrent.TimeUnit

// add frequently used parameters

fun String.toTitleParam() = Pair(AnalyticsParamNames.title, this)

fun Long.toTimeParam() = Pair(
    AnalyticsParamNames.time,
    TimeUnit.MILLISECONDS.toSeconds(this).toString()
)