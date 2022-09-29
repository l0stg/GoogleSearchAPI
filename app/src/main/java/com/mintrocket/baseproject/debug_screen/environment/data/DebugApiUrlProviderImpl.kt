package com.mintrocket.baseproject.debug_screen.environment.data

import com.mintrocket.data.api.ApiUrlProvider

class DebugApiUrlProviderImpl(
    private val environmentRepository: EnvironmentRepository
) : ApiUrlProvider {

    override fun getBaseUrl(): String {
        return environmentRepository.getSelectedEntry().url
    }
}