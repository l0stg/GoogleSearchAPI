package com.mintrocket.modules.security.biometric

data class BiometricRequest(
    val title: String,
    val description: String,
    val negativeButton: String
)