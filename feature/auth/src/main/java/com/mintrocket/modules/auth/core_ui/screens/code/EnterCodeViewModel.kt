package com.mintrocket.modules.auth.core_ui.screens.code

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.datacore.Event
import com.mintrocket.datacore.coroutines.SerialJob
import com.mintrocket.datacore.errorhandling.WrongCodeException
import com.mintrocket.datacore.extensions.into
import com.mintrocket.modules.auth.core_ui.AuthTypeConfig
import com.mintrocket.modules.auth.core_ui.data.model.SendCodeResult
import com.mintrocket.modules.auth.core_ui.data.repository.IAuthCodeRepository
import com.mintrocket.modules.auth.core_ui.external.AuthErrorHandler
import com.mintrocket.modules.auth.core_ui.external.IAuthRouter
import com.mintrocket.uicore.event
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.DurationUnit

internal class EnterCodeViewModel(
    private val phone: String,
    private val config: AuthTypeConfig.PhoneAndCode,
    private val router: IAuthRouter,
    private val errorHandler: AuthErrorHandler,
    private val authCodeRepository: IAuthCodeRepository,
) : ViewModel() {

    companion object{
        private const val TIMER_DELAY = 1000L

    }

    private val timeToResend = MutableLiveData<Int>()
    private val progress = MutableLiveData(false)
    private val incorrectCode = MutableLiveData(false)
    private val sendCodeEnabled = MutableLiveData(false)

    private val tickerJob = SerialJob()

    private val externalCode = MutableLiveData<Event<String>>()

    private val coroutineErrorHandler = CoroutineExceptionHandler { _, throwable ->
        progress.postValue(false)
        if (throwable is WrongCodeException) {
            incorrectCode.postValue(true)
        } else {
            errorHandler.handleError(throwable)
        }
    }

    init {
        sendCode()
    }

    fun onBackPressed() {
        router.popScreen()
    }

    fun timeToResend(): LiveData<Int> = timeToResend

    fun progress(): LiveData<Boolean> = progress

    fun incorrectCode(): LiveData<Boolean> = incorrectCode

    fun externalCode(): LiveData<Event<String>> = externalCode

    fun sendCodeEnabled(): LiveData<Boolean> = sendCodeEnabled

    fun onResendClicked() {
        if (timeToResend.value == 0) {
            sendCode()
        }
    }

    fun onCodeChanged(code: String) {
        incorrectCode.value = false
        val codeFilled = code.length == config.codeLength
        if (codeFilled && !config.confirmCodeWithButton) {
            checkCode(code)
        } else {
            sendCodeEnabled.value = codeFilled
        }
    }

    fun onCheckCodeClicked(code: String) {
        checkCode(code)
    }

    private fun checkCode(code: String) {
        viewModelScope.launch(coroutineErrorHandler) {
            progress.value = true
            withContext(Dispatchers.IO) {
                authCodeRepository.checkCode(phone, code)
            }
            router.openOnSuccess()
        }
    }

    fun onCodeMessageReceived(message: String?) {
        val regex = config.codeInMessageRegex
        regex.find(message.orEmpty())?.groups?.get(1)?.value?.also { code ->
            externalCode.event = code
        }
    }

    private fun startTicker(delaySec: Int) {
        viewModelScope.launch {
            timeToResend.value = delaySec
            val ticker = ticker(TIMER_DELAY, TIMER_DELAY)
            ticker.consumeEach {
                timeToResend.value = (timeToResend.value ?: delaySec) - 1
                if (timeToResend.value == 0) tickerJob.cancel()
            }
        } into tickerJob
    }

    private fun sendCode() {
        viewModelScope.launch(coroutineErrorHandler) {
            progress.value = true
            val sendResult = withContext(Dispatchers.IO) {
                authCodeRepository.sendCode(phone)
            }
            if (sendResult is SendCodeResult.Sent) {
                val delaySec = TimeUnit.MILLISECONDS.toSeconds(sendResult.timeToResend).toInt()
                startTicker(delaySec)
            }
            progress.value = false
        }
    }
}