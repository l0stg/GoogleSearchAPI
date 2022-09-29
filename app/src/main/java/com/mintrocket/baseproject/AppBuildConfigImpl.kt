package com.mintrocket.baseproject

import com.mintrocket.datacore.AppBuildConfig
import java.util.*

class AppBuildConfigImpl : AppBuildConfig {
    override val isDebug: Boolean = BuildConfig.DEBUG
    override val applicationId: String = BuildConfig.APPLICATION_ID
    override val buildType: String = BuildConfig.BUILD_TYPE
    override val flavor: String = BuildConfig.FLAVOR
    override val versionCode: Int = BuildConfig.VERSION_CODE
    override val versionName: String = BuildConfig.VERSION_NAME
    override val buildDate: Date = BuildConfig.BUILD_DATE
    override val baseUrl: String = BuildConfig.BASE_URL
    override val isDebugScreenEnabled: Boolean = BuildConfig.DEBUG_SCREEN_ENABLED
}