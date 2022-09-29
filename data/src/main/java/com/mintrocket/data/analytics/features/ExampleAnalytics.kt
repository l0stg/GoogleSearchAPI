package com.mintrocket.data.analytics.features

import com.mintrocket.data.analytics.AnalyticsEventNames
import com.mintrocket.data.analytics.AnalyticsParamNames
import com.mintrocket.data.analytics.extensions.toTimeParam
import com.mintrocket.data.analytics.extensions.toTitleParam
import com.mintrocket.data.analytics.sender.AnalyticsSender

class ExampleAnalytics(private val sender: AnalyticsSender) {

    fun exampleSend(title: String, time: Long, id: String) {
        sender.send(
            AnalyticsEventNames.example_event,
            title.toTitleParam(),
            time.toTimeParam(),
            AnalyticsParamNames.id to id
        )
    }

}