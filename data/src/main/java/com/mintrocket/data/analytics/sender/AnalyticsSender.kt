package com.mintrocket.data.analytics.sender

interface AnalyticsSender {

    fun send(key: String, vararg params: Pair<String, String>)
}