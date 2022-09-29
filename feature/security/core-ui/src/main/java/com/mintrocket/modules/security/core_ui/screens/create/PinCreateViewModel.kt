package com.mintrocket.modules.security.core_ui.screens.create

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.datacore.coroutines.SerialJob
import com.mintrocket.datacore.extensions.into
import com.mintrocket.modules.security.external.SecurityErrorHandler
import com.mintrocket.modules.security.screens.pin_create.PinCreateScreenPart
import com.mintrocket.datacore.Event
import com.mintrocket.uicore.toLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PinCreateViewModel(
    private val screenPart: PinCreateScreenPart,
    private val errorHandler: SecurityErrorHandler
) : ViewModel() {

    private var pinJob = SerialJob()

    private val titleState = screenPart.titleState()
        .toLiveData(viewModelScope.coroutineContext)

    private val clearAction = screenPart.clearAction()
        .toLiveData(viewModelScope.coroutineContext)

    private val coroutinesErrorHandler = CoroutineExceptionHandler { _, ex ->
        errorHandler.handleError(ex)
    }

    fun titleState(): LiveData<PinCreateScreenPart.TitleState> = titleState

    fun clearAction(): LiveData<Event<Unit>> = clearAction

    fun onBackPressed() = screenPart.onBackPressed()

    fun onValueChanged(value: String, filled: Boolean) {
        viewModelScope.launch(coroutinesErrorHandler) {
            screenPart.onValueChanged(value, filled)
        } into pinJob
    }
}