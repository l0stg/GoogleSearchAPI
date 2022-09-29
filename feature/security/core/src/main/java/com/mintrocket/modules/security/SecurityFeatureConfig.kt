package com.mintrocket.modules.security

import java.util.concurrent.TimeUnit

data class SecurityFeatureConfig(
    val preferencesName: String = SECURITY_PREFERENCES,
    val pinCodeKey: String = KEY_BIOMETRIC,
    val biometricKey: String = KEY_PIN_CODE,

    val pinDotsCount: Int = 4,

    val launchBarrierTag: String = TAG_PIN_BARRIER,
    val timeoutInMillis: Long = securityTimeout,

    val settingsCheckTag: String = TAG_PIN_CHECK
) {

    companion object {
        private const val SECURITY_PREFERENCES = "security_pref"
        private const val KEY_PIN_CODE = "pin_code"
        private const val KEY_BIOMETRIC = "biometric"

        private const val TAG_PIN_BARRIER = "SecurityFeatureConfig:pin_barrier"
        private val securityTimeout = TimeUnit.MINUTES.toMillis(5)

        private const val TAG_PIN_CHECK = "SecurityFeatureConfig:pin_check"
    }

}