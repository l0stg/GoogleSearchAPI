package com.mintrocket.modules.security.screens.fingerprint

import com.mintrocket.modules.security.biometric.BiometricController
import com.mintrocket.modules.security.biometric.BiometricRequest
import com.mintrocket.modules.security.biometric.BiometricResult
import com.mintrocket.modules.security.data.SecurityRepository
import com.mintrocket.modules.security.external.FingerprintRouter
import com.mintrocket.modules.security.external.VibrationController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class FingerprintScreenPart(
    private val securityRepository: SecurityRepository,
    private val biometricController: BiometricController,
    private val vibrationController: VibrationController,
    private val router: FingerprintRouter,
    private val biometricRequest: BiometricRequest
) {

    fun onBackPressed() {
        router.exit()
    }

    suspend fun onAcceptClick() {
        val enabled = withContext(Dispatchers.IO) {
            securityRepository.pinCodeEnabled().first()
        }
        if (!enabled || !biometricController.isSupported()) {
            router.exit()
            return
        }
        val result = biometricController.authenticate(biometricRequest)
        when (result) {
            is BiometricResult.Success -> {
                withContext(Dispatchers.IO) {
                    securityRepository.setBiometricEnabled(true)
                }
                router.exit()
            }
            is BiometricResult.Error -> {
                vibrationController.vibrateWrongCode()
                router.showMsg(result.msg)
            }
        }
    }

    fun onCancelClick() {
        router.exit()
    }
}