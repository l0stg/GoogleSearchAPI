package com.mintrocket.baseproject.debug_screen.logreader.data

data class LogCatFilter(
    val query: String,
    val levels: List<Int>,
    val onlyLatestPid: Boolean
)