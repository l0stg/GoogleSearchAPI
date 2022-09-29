package com.mintrocket.datacore

import java.util.*

interface AppBuildConfig {
    val isDebug: Boolean
    val applicationId: String
    val buildType: String
    val flavor: String
    val versionCode: Int
    val versionName: String
    val buildDate: Date
    val baseUrl: String
    val isDebugScreenEnabled: Boolean
}