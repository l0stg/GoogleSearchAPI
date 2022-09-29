package com.mintrocket.baseproject.debug_screen

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.mintrocket.baseproject.R

class DebugScreenHelper(
    private val debugScreenEnabled: Boolean
) {

    companion object {
        private const val DEBUG_CHANNEL_ID = "debug_channel"
        private const val DEBUG_CHANNEL_NAME = "debug_channel"
        private const val NOTIFICATION_ID = 2323
        private const val PENDING_INTENT_CODE = 2334
    }

    fun showDebugNotificationIfRequired(context: Context) {
        if (!debugScreenEnabled) return
        val notificationManager = NotificationManagerCompat.from(context)
        createChannel(notificationManager)

        val pendingFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_CANCEL_CURRENT
        }
        val pIntent = PendingIntent.getActivity(
            context,
            PENDING_INTENT_CODE,
            DebugScreenActivity.newIntent(context),
            pendingFlags
        )

        val notification = NotificationCompat
            .Builder(context, DEBUG_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_debug_notification)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.debug_open_debug_screen))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pIntent)
            .setSilent(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createChannel(notificationManager: NotificationManagerCompat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                DEBUG_CHANNEL_ID,
                DEBUG_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
}