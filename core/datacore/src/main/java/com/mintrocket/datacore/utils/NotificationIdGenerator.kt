package com.mintrocket.datacore.utils

import android.content.Context

class NotificationIdGenerator(
    context: Context
) {

    companion object {
        private const val KEY_LATEST_ID = "key_latest_id"
    }

    private val prefs =
        context.getSharedPreferences("NotificationIdGeneratorPreferences", Context.MODE_PRIVATE)

    fun getUniqueNotificationId(): Int {
        val id = prefs.getInt(KEY_LATEST_ID, 0)
        prefs.edit()
            .putInt(KEY_LATEST_ID, id + 1)
            .apply()

        return id
    }
}