package com.mintrocket.modules.security.launch

import android.os.SystemClock
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mintrocket.modules.security.SecurityFeatureConfig
import com.mintrocket.modules.security.data.SecurityRepository
import com.mintrocket.modules.security.external.SecurityErrorHandler
import com.mintrocket.modules.security.external.SecurityLaunchRouter
import com.mintrocket.modules.security.screens.PinCheckResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class SecurityLaunchController(
    private val config: SecurityFeatureConfig,
    private val securityRepository: SecurityRepository,
    private val router: SecurityLaunchRouter,
    private val checkResultFlow: Flow<PinCheckResult>,
    private val errorHandler: SecurityErrorHandler
) : DefaultLifecycleObserver {

    private var isFirstForeground = true
    private var appStarted: Boolean = false
    private var hiddenAt: Long? = null

    private val accessFlow = MutableStateFlow(false)

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var securityCheckJob: Job? = null

    private val coroutinesErrorHandler = CoroutineExceptionHandler { _, ex ->
        errorHandler.handleError(ex)
    }

    /*
    * Событие придёт только если будет дано добро на запуск
    * */
    fun observeAccess(): Flow<Unit> = accessFlow.filter { it }.map { Unit }

    /*
    * Будут приходить все события по смене статуса доступа к аппке
    * */
    fun observeAccessState(): Flow<Boolean> = accessFlow

    /*
    * Вызывать, когда приложение будет считаться запущенным и готовым к работе, после проверки пинкода.
    * Сейчас - это после того, как обработается deeplink в MainActivity
    * Если будут домофоны, то скорее всего нужно будет после того, как в активити домофон-звонилки проверится пинкод.
    * */
    fun onAppStarted() {
        appStarted = true
    }

    fun isAppStarted() = appStarted

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (isFirstForeground) {
            isFirstForeground = false
            return
        }
        requireCheck()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        if (securityCheckJob?.isActive == true) return
        coroutineScope.launch {
            val pinCodeEnabled = getPinCodeEnabled()
            if (pinCodeEnabled && hiddenAt == null) {
                hiddenAt = SystemClock.elapsedRealtime()
            }
        }
    }

    /*
    * Нужно, чтобы указать, что приложение считается закрытым и нужно проверить пинкод при старте
    * */
    fun onAppFinishing() {
        appStarted = false
        accessFlow.value = false
    }

    /*
    * Запрашиваем проверку. Результат проверки будет отправлен в accessFlow
    * Сначала нужно подписаться на observeAccess, а потом уже вызывать requireCheck
    * */
    fun requireCheck() {
        if (securityCheckJob?.isActive == true) return
        accessFlow.value = false
        securityCheckJob = coroutineScope.launch(coroutinesErrorHandler) {
            val pinCodeEnabled = getPinCodeEnabled()
            val isExpired = needSecurityCheckByTimeout()
            if (!pinCodeEnabled) {
                accessFlow.value = true
                return@launch
            }
            if (!isExpired && appStarted) {
                accessFlow.value = true
                return@launch
            }

            router.openBarrier()

            when (getSecurityCheckResult()) {
                PinCheckResult.Success -> accessFlow.value = true
                is PinCheckResult.Cancel -> {
                    onAppFinishing()
                    accessFlow.value = false
                }
            }
            hiddenAt = null
        }
    }

    private suspend fun getSecurityCheckResult() = checkResultFlow.first()

    private suspend fun getPinCodeEnabled() = withContext(Dispatchers.IO) {
        securityRepository.pinCodeEnabled().first()
    }

    private fun needSecurityCheckByTimeout(): Boolean {
        val hiddenTimeMillis = hiddenAt ?: return false
        val hiddenDelta = SystemClock.elapsedRealtime() - hiddenTimeMillis
        return if (hiddenDelta < config.timeoutInMillis) {
            hiddenAt = null
            false
        } else {
            true
        }
    }
}