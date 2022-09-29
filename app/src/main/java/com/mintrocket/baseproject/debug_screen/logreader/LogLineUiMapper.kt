package com.mintrocket.baseproject.debug_screen.logreader

import android.content.Context
import android.util.Log
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.debug_screen.logreader.data.LogCatLine
import com.mintrocket.uicore.getColorByAttr
import java.text.SimpleDateFormat
import java.util.*


private val DATE_FORMAT = SimpleDateFormat("HH:mm:ss.SSS", Locale.US)

fun LogCatLine.formatDate(): String? = DATE_FORMAT.format(date)

fun LogCatLine.toLevelName() = when (level) {
    Log.DEBUG -> "Debug"
    Log.ERROR -> "Error"
    Log.INFO -> "Info"
    Log.WARN -> "Warning"
    Log.VERBOSE -> "Verbose"
    Log.ASSERT -> "Assert"
    else -> "Unknown"
}

fun LogCatLine.toLevelColor(context: Context): Int = when (level) {
    Log.DEBUG -> context.getColor(R.color.debug_log_line_debug)
    Log.ERROR -> context.getColor(R.color.debug_log_line_error)
    Log.INFO -> context.getColor(R.color.debug_log_line_info)
    Log.WARN -> context.getColor(R.color.debug_log_line_warn)
    Log.ASSERT -> context.getColor(R.color.debug_log_line_assert)
    else -> context.getColorByAttr(android.R.attr.textColorPrimary)
}