package com.mintrocket.baseproject.debug_screen.environment

import com.mintrocket.baseproject.debug_screen.environment.data.EnvironmentEntry

data class EnvironmentScreenState(
    val entries: List<EnvironmentEntry> = emptyList(),
    val selectedEntryId: String? = null
)