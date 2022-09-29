package com.mintrocket.modules.security.core_ui.screens.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.modules.security.external.SecurityErrorHandler
import com.mintrocket.modules.security.screens.PinCheckResult
import com.mintrocket.modules.security.screens.settings.SecuritySettingsScreenPart
import com.mintrocket.uicore.toLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SecuritySettingsViewModel(
    private val screenPart: SecuritySettingsScreenPart,
    private val errorHandler: SecurityErrorHandler,
    private val resultsBuffer: Flow<PinCheckResult>
) : ViewModel() {

    private var pinCodeJob: Job? = null
    private var biometricJob: Job? = null

    private val pinCodeState = screenPart.pinCodeState()
        .toLiveData(viewModelScope.coroutineContext)

    private val biometricState = screenPart.biometricState()
        .toLiveData(viewModelScope.coroutineContext)

    private val biometricEnabled = screenPart.biometricEnabled()
        .toLiveData(viewModelScope.coroutineContext)

    private val coroutinesErrorHandler = CoroutineExceptionHandler { _, ex ->
        errorHandler.handleError(ex)
    }

    init {
        subscribeToResults()
    }

    fun pinCodeState(): LiveData<Boolean> = pinCodeState

    fun biometricState(): LiveData<Boolean> = biometricState

    fun biometricEnabled(): LiveData<Boolean> = biometricEnabled

    fun onBackPressed() = screenPart.onBackPressed()

    fun onPinCodeClick(checked: Boolean) {
        if (pinCodeJob?.isActive == true) return
        pinCodeJob = viewModelScope.launch(coroutinesErrorHandler) {
            screenPart.onPinCodeClick(checked)
        }
    }

    fun onBiometricClick(checked: Boolean) {
        if (biometricJob?.isActive == true) return
        biometricJob = viewModelScope.launch(coroutinesErrorHandler) {
            screenPart.onBiometricClick(checked)
        }
    }

    private fun subscribeToResults() {
        resultsBuffer
            .filterIsInstance<PinCheckResult.Success>()
            .onEach {
                viewModelScope.launch(coroutinesErrorHandler) {
                    screenPart.onSuccessCheck()
                }
            }
            .launchIn(viewModelScope)
    }
}