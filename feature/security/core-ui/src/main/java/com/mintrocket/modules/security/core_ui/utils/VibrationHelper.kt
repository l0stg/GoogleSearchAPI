package com.mintrocket.modules.security.core_ui.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object VibrationHelper {

    fun oneShot(context: Context, durationMs: Long = 350) {
        withVibrator(context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val effect = VibrationEffect.createOneShot(
                    durationMs,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
                vibrate(effect)
            } else {
                vibrate(durationMs)
            }
        }
    }

    private fun withVibrator(context: Context, block: Vibrator.() -> Unit) {
        val vibrator = getVibrator(context)
        if (vibrator != null && vibrator.hasVibrator()) {
            block.invoke(vibrator)
        }
    }

    private fun getVibrator(context: Context) =
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
}