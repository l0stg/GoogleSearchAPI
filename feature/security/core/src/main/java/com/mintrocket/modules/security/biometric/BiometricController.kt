package com.mintrocket.modules.security.biometric

interface BiometricController {

    suspend fun isSupported(): Boolean

    suspend fun authenticate(request: BiometricRequest): BiometricResult
}