package com.mintrocket.modules.security.core_ui.screens.fingerprint

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.modules.security.external.SecurityErrorHandler
import com.mintrocket.modules.security.screens.fingerprint.FingerprintScreenPart
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FingerprintViewModel(
    private val screenPart: FingerprintScreenPart,
    private val errorHandler: SecurityErrorHandler,
) : ViewModel() {

    private var biometricJob: Job? = null

    private val coroutinesErrorHandler = CoroutineExceptionHandler { _, ex ->
        errorHandler.handleError(ex)
    }

    fun onBackPressed() = screenPart.onBackPressed()

    fun onAcceptClick() {
        if (biometricJob?.isActive == true) return
        biometricJob = viewModelScope.launch(coroutinesErrorHandler) {
            screenPart.onAcceptClick()
        }
    }

    fun onCancelClick() = screenPart.onCancelClick()
}