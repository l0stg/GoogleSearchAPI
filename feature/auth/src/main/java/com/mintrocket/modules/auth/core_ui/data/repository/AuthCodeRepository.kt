package com.mintrocket.modules.auth.core_ui.data.repository

import com.mintrocket.datacore.errorhandling.PhoneNotFoundException
import com.mintrocket.modules.auth.core_ui.AuthTypeConfig
import com.mintrocket.modules.auth.core_ui.data.local.SentCodeDataSource
import com.mintrocket.modules.auth.core_ui.data.model.SendCodeResult
import com.mintrocket.modules.auth.core_ui.data.model.SentCode
import com.mintrocket.modules.auth.core_ui.external.IAuthCodeDataSource

internal class AuthCodeRepository(
    private val config: AuthTypeConfig.PhoneAndCode,
    private val authDataSource: IAuthCodeDataSource,
    private val sentCodeDataSource: SentCodeDataSource
) : IAuthCodeRepository {

    override suspend fun checkCode(phone: String, code: String) {
        authDataSource.checkCode(phone, code)
    }

    override suspend fun sendCode(phone: String): SendCodeResult {
        return findRecentlySentCode(phone)?:tryNewSendCode(phone)
    }

    private suspend fun tryNewSendCode(phone: String): SendCodeResult = try {
        authDataSource.sendCode(phone)
        sentCodeDataSource.save(SentCode(phone, System.currentTimeMillis()))
        SendCodeResult.Sent(config.resendCodeDelayMillis)
    } catch (ex: Exception) {
        //todo parse api exception
        if (ex is PhoneNotFoundException) {
            SendCodeResult.NotRegistered
        } else {
            throw ex
        }
    }

    private suspend fun findRecentlySentCode(phone: String): SendCodeResult.Sent? {
        sentCodeDataSource.get(phone)?.let {
            val resendTime = it.sentAt + config.resendCodeDelayMillis
            if (resendTime > System.currentTimeMillis()) {
                return SendCodeResult.Sent(resendTime - System.currentTimeMillis())
            }
        }
        return null
    }
}