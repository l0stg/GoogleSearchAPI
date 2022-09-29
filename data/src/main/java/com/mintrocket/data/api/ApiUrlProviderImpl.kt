package com.mintrocket.data.api

import com.mintrocket.datacore.AppBuildConfig

class ApiUrlProviderImpl(private val buildConfig: AppBuildConfig) : ApiUrlProvider {

    override fun getBaseUrl(): String = buildConfig.baseUrl
}