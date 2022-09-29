package com.mintrocket.modules.security.core_ui.screens.check

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.datacore.coroutines.SerialJob
import com.mintrocket.datacore.extensions.into
import com.mintrocket.modules.security.external.SecurityErrorHandler
import com.mintrocket.modules.security.screens.pin_check.PinCheckScreenPart
import com.mintrocket.datacore.Event
import com.mintrocket.uicore.toLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PinCheckViewModel(
    private val screenPart: PinCheckScreenPart,
    private val errorHandler: SecurityErrorHandler
) : ViewModel() {

    private var biometricJob: Job? = null
    private var pinJob = SerialJob()

    private val biometricEnabled = screenPart.biometricEnabled()
        .toLiveData(viewModelScope.coroutineContext)

    private val clearAction = screenPart.clearAction()
        .toLiveData(viewModelScope.coroutineContext)

    private val coroutinesErrorHandler = CoroutineExceptionHandler { _, ex ->
        errorHandler.handleError(ex)
    }

    fun biometricEnabled(): LiveData<Boolean> = biometricEnabled

    fun clearAction(): LiveData<Event<Unit>> = clearAction

    fun onBackPressed() {
        screenPart.onBackPressed()
    }

    fun onPinChanged(value: String, filled: Boolean) {
        viewModelScope.launch(coroutinesErrorHandler) {
            screenPart.onPinChanged(value, filled)
        } into pinJob
    }

    fun requireBiometric() {
        if (biometricJob?.isActive == true) return
        biometricJob = viewModelScope.launch(coroutinesErrorHandler) {
            screenPart.requireBiometric()
        }
    }
}