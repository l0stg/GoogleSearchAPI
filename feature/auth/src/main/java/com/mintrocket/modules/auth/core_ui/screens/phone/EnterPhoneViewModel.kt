package com.mintrocket.modules.auth.core_ui.screens.phone

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.modules.auth.core_ui.data.model.SendCodeResult
import com.mintrocket.modules.auth.core_ui.data.repository.IAuthCodeRepository
import com.mintrocket.modules.auth.core_ui.external.AuthErrorHandler
import com.mintrocket.modules.auth.core_ui.external.IAuthRouter
import com.mintrocket.modules.auth.core_ui.screens.code.EnterCodeFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


internal class EnterPhoneViewModel(
    private val router: IAuthRouter,
    private val errorHandler: AuthErrorHandler,
    private val authCodeRepository: IAuthCodeRepository,
) : ViewModel() {

    private val phoneFilled = MutableLiveData(false)
    private val progress = MutableLiveData(false)
    private var inputPhone = ""

    private val coroutineErrorHandler = CoroutineExceptionHandler { _, throwable ->
        progress.postValue(false)
        errorHandler.handleError(throwable)
    }

    fun phoneFilled(): LiveData<Boolean> = phoneFilled

    fun progress(): LiveData<Boolean> = progress

    fun onInputChanged(phone: String, filled: Boolean) {
        inputPhone = phone
        phoneFilled.value = filled
    }

    fun onSendCodeClicked() {
        viewModelScope.launch(coroutineErrorHandler) {
            progress.value = true
            when (authCodeRepository.sendCode(inputPhone)) {
                is SendCodeResult.Sent -> {
                    router.openFragment { EnterCodeFragment.newInstance(inputPhone) }
                }
                is SendCodeResult.NotRegistered -> {
                    //todo open registration
                }
            }
            progress.value = false
        }
    }

    fun onLegalClick(legalId: String) {
        router.openLegalDoc(legalId)
    }
}