package com.mintrocket.baseproject.debug_screen.logreader.data

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class LogParser {

    companion object {
        private val LINE_REGEX =
            Regex("""^(\d{2}-\d{2} \d{2}:\d{2}:\d{2}.\d{3})\s+(\d+)\s+(\d+)\s+([A-Z])\s+(.+?)\s*: (.*)$""")
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)

        private const val INDEX_DATE = 1
        private const val INDEX_PID = 2
        private const val INDEX_TID = 3
        private const val INDEX_LEVEL = 4
        private const val INDEX_TAG = 5
        private const val INDEX_MESSAGE = 6
    }

    fun parse(logString: String): LogCatLine? = LINE_REGEX.find(logString)?.let {
        val rawDate = requireNotNull(it.groups[INDEX_DATE]?.value) { "Not found date" }
        val rawPid = requireNotNull(it.groups[INDEX_PID]?.value) { "Not found pid" }
        val rawTid = requireNotNull(it.groups[INDEX_TID]?.value) { "Not found tid" }
        val rawLevel = requireNotNull(it.groups[INDEX_LEVEL]?.value) { "Not found level" }
        val rawTag = requireNotNull(it.groups[INDEX_TAG]?.value) { "Not found tag" }
        val rawMessage = requireNotNull(it.groups[INDEX_MESSAGE]?.value) { "Not found message" }

        val date = parseDate(rawDate)
        val pid = rawPid.toInt()
        val tid = rawTid.toInt()
        val level = parseLevel(rawLevel)
        LogCatLine(
            id = System.nanoTime(),
            date = date,
            pid = pid,
            tid = tid,
            level = level,
            tag = rawTag,
            message = rawMessage
        )
    }

    private fun parseLevel(rawLevel: String): Int = when (rawLevel) {
        "D" -> Log.DEBUG
        "E" -> Log.ERROR
        "I" -> Log.INFO
        "W" -> Log.WARN
        "V" -> Log.VERBOSE
        "F" -> Log.ASSERT
        else -> Log.ERROR
    }

    private fun parseDate(rawDate: String): Date {
        val year = LocalDate.now().year
        return requireNotNull(DATE_FORMAT.parse("$year-$rawDate")) {
            "Invalid date format"
        }
    }
}