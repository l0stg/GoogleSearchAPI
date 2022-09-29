package com.mintrocket.modules.security.screens.pin_create

import com.mintrocket.modules.security.biometric.BiometricController
import com.mintrocket.modules.security.data.SecurityRepository
import com.mintrocket.modules.security.external.PinCreateRouter
import com.mintrocket.modules.security.external.VibrationController
import com.mintrocket.datacore.Event
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.withContext

class PinCreateScreenPart(
    private val biometricController: BiometricController,
    private val vibrationController: VibrationController,
    private val securityRepository: SecurityRepository,
    private val router: PinCreateRouter
) {

    private var pinCodeValue = ""
    private var checkPinCodeValue = ""
    private var pinCodeFilled = false

    private val titleState = MutableStateFlow(TitleState.CREATE)
    private val clearAction = BroadcastChannel<Event<Unit>>(1)

    init {
        updateTitle()
    }

    fun titleState(): Flow<TitleState> = titleState

    fun clearAction(): Flow<Event<Unit>> = clearAction.asFlow()

    fun onBackPressed() {
        router.exit()
    }

    suspend fun onValueChanged(value: String, filled: Boolean) {
        if (!pinCodeFilled) {
            pinCodeFilled = filled
            pinCodeValue = value
            if (filled) {
                clearAction.send(Event(Unit))
            }
        } else {
            checkPinCodeValue = value
            if (filled) {
                checkPinCode()
            }
        }
        updateTitle()
    }

    private suspend fun checkPinCode() {
        if (pinCodeValue == checkPinCodeValue) {
            withContext(Dispatchers.IO) { securityRepository.saveCode(pinCodeValue) }
            val biometricSupported = biometricController.isSupported()
            if (biometricSupported) {
                router.replaceFingerprint()
            } else {
                router.exit()
            }
        } else {
            pinCodeValue = ""
            checkPinCodeValue = ""
            pinCodeFilled = false
            clearAction.send(Event(Unit))
            vibrationController.vibrateWrongCode()
            router.showWrongCode()
        }
    }

    private fun updateTitle() {
        titleState.value = if (!pinCodeFilled) {
            TitleState.CREATE
        } else {
            TitleState.REPEAT
        }
    }

    enum class TitleState {
        CREATE, REPEAT
    }
}