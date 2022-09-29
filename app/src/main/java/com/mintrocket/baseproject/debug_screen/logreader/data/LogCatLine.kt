package com.mintrocket.baseproject.debug_screen.logreader.data

import java.util.*

data class LogCatLine(
    val id: Long,
    val date: Date,
    val pid: Int,
    val tid: Int,
    val level: Int,
    val tag: String,
    val message: String
)


