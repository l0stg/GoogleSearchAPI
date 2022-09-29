package com.mintrocket.modules.security.screens.settings

import com.mintrocket.modules.security.biometric.BiometricController
import com.mintrocket.modules.security.biometric.BiometricRequest
import com.mintrocket.modules.security.biometric.BiometricResult
import com.mintrocket.modules.security.data.SecurityRepository
import com.mintrocket.modules.security.external.SecuritySettingsRouter
import com.mintrocket.modules.security.external.VibrationController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SecuritySettingsScreenPart(
    private val securityRepository: SecurityRepository,
    private val biometricController: BiometricController,
    private val vibrationController: VibrationController,
    private val router: SecuritySettingsRouter,
    private val biometricRequest: BiometricRequest
) {

    private val pinCodeState = securityRepository
        .pinCodeEnabled()

    private val biometricState = securityRepository
        .biometricEnabled()

    private val biometricEnabled = MutableStateFlow(false)
        .map { biometricController.isSupported() }

    fun pinCodeState(): Flow<Boolean> = pinCodeState

    fun biometricState(): Flow<Boolean> = biometricState

    fun biometricEnabled(): Flow<Boolean> = biometricEnabled

    fun onBackPressed() {
        router.exit()
    }

    suspend fun onPinCodeClick(checked: Boolean) {
        val pinCodeEnabled = securityRepository.pinCodeEnabled().first()
        if (pinCodeEnabled == checked) return

        if (pinCodeEnabled) {
            router.openCheck()
        } else {
            router.openCreate()
        }
    }

    suspend fun onBiometricClick(checked: Boolean) {
        val biometricEnabled = securityRepository.biometricEnabled().first()
        val pinCodeEnabled = securityRepository.pinCodeEnabled().first()
        if (biometricEnabled == checked) return

        if (!pinCodeEnabled) {
            router.showSuggestToBiometricDialog {
                router.openCreate()
            }
            return
        }

        if (biometricEnabled) {
            saveBiometricEnabled(false)
        } else {
            requireBiometric()
        }
    }

    suspend fun onSuccessCheck() = withContext(Dispatchers.IO) {
        securityRepository.clear()
    }

    private suspend fun requireBiometric() {
        val result = biometricController.authenticate(biometricRequest)
        when (result) {
            is BiometricResult.Success -> saveBiometricEnabled(true)
            is BiometricResult.Error -> {
                vibrationController.vibrateWrongCode()
                router.showMsg(result.msg)
            }
        }
    }

    private suspend fun saveBiometricEnabled(enabled: Boolean) {
        securityRepository.setBiometricEnabled(enabled)
    }

}