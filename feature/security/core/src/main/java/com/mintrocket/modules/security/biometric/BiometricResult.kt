package com.mintrocket.modules.security.biometric

sealed class BiometricResult {
    object Success : BiometricResult()
    object Cancel : BiometricResult()
    data class Error(val msg: String) : BiometricResult()
}