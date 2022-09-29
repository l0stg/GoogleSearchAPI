package com.mintrocket.modules.auth.core_ui.screens.login_pass

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.datacore.errorhandling.WrongCredentialsException
import com.mintrocket.modules.auth.core_ui.AuthTypeConfig
import com.mintrocket.modules.auth.core_ui.external.AuthErrorHandler
import com.mintrocket.modules.auth.core_ui.external.IAuthLoginPassDataSource
import com.mintrocket.modules.auth.core_ui.external.IAuthRouter
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginPassAuthViewModel(
    private val config: AuthTypeConfig.LoginAndPass,
    private val router: IAuthRouter,
    private val errorHandler: AuthErrorHandler,
    private val dataSource: IAuthLoginPassDataSource
) : ViewModel() {

    private val coroutineErrorHandler = CoroutineExceptionHandler { _, throwable ->
        _progress.postValue(false)
        if (throwable is WrongCredentialsException) {
            _fieldsState.postValue(LoginAndPassState.WrongCredentials(config.wrongCredsErrorMessage))
        } else {
            errorHandler.handleError(throwable)
        }
    }

    private val _buttonState = MutableLiveData(false)
    val buttonState: LiveData<Boolean> = _buttonState

    private val _fieldsState = MutableLiveData<LoginAndPassState>(LoginAndPassState.Empty)
    val fieldsState: LiveData<LoginAndPassState> = _fieldsState

    private val _progress = MutableLiveData(false)
    val progress: LiveData<Boolean> = _progress

    fun onInputChanged(login: String, pass: String) {
        _buttonState.value = login.isNotEmpty()
                && pass.length >= config.minPassLength
    }

    fun auth(login: String, pass: String) {
        if (config.loginRegex?.matches(login) == false) {
            _fieldsState.value = LoginAndPassState.InvalidLoginFormat(config.loginRegexErrorMessage)
            return
        }

        viewModelScope.launch(coroutineErrorHandler) {
            withContext(Dispatchers.IO) {
                dataSource.auth(login, pass)
            }

            router.openOnSuccess()
        }
    }

    fun onLegalClicked(legalId: String) {
        router.openLegalDoc(legalId)
    }
}