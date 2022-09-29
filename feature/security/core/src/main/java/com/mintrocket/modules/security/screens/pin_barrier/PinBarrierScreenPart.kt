package com.mintrocket.modules.security.screens.pin_barrier

import com.mintrocket.datacore.Event
import com.mintrocket.modules.security.biometric.BiometricController
import com.mintrocket.modules.security.biometric.BiometricRequest
import com.mintrocket.modules.security.biometric.BiometricResult
import com.mintrocket.modules.security.data.SecurityRepository
import com.mintrocket.modules.security.external.PinBarrierRouter
import com.mintrocket.modules.security.external.SecurityLogoutUseCase
import com.mintrocket.modules.security.external.VibrationController
import com.mintrocket.modules.security.screens.CheckResultReason
import com.mintrocket.modules.security.screens.PinCheckResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PinBarrierScreenPart(
    private val securityRepository: SecurityRepository,
    private val biometricController: BiometricController,
    private val vibrationController: VibrationController,
    private val logoutUseCase: SecurityLogoutUseCase,
    private val router: PinBarrierRouter,
    private val biometricRequest: BiometricRequest,
) {

    private val biometricEnabled = securityRepository
        .biometricEnabled()
        .map { it && biometricController.isSupported() }

    private val clearAction = BroadcastChannel<Event<Unit>>(1)

    fun biometricEnabled(): Flow<Boolean> = biometricEnabled

    fun clearAction(): Flow<Event<Unit>> = clearAction.asFlow()

    suspend fun onLogoutClick() {
        withContext(Dispatchers.IO) { logoutUseCase.logout() }
        router.exit(PinCheckResult.Cancel(CheckResultReason.LOGOUT))
    }

    fun onBackPressed() {
        router.exit(PinCheckResult.Cancel())
    }

    suspend fun onPinChanged(value: String, filled: Boolean) {
        if (!filled) return
        val isValidCode = withContext(Dispatchers.IO) {
            securityRepository.checkCode(value)
        }
        if (isValidCode) {
            router.exit(PinCheckResult.Success)
        } else {
            clearAction.send(Event(Unit))
            vibrationController.vibrateWrongCode()
            router.showWrongCode()
        }
    }

    suspend fun requireBiometric() {
        val enabled = withContext(Dispatchers.IO) {
            securityRepository.biometricEnabled().first()
        }
        if (!enabled || !biometricController.isSupported()) return
        val result = biometricController.authenticate(biometricRequest)
        when (result) {
            is BiometricResult.Success -> router.exit(PinCheckResult.Success)
            is BiometricResult.Error -> {
                clearAction.send(Event(Unit))
                vibrationController.vibrateWrongCode()
                router.showMsg(result.msg)
            }
        }
    }
}