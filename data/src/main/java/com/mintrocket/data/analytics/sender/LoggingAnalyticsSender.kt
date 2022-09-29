package com.mintrocket.data.analytics.sender

import timber.log.Timber

class LoggingAnalyticsSender : AnalyticsSender {

    override fun send(key: String, vararg params: Pair<String, String>) {
        Timber.d("key: $key, params: ${params.toMap()}")
    }
}