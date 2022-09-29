package com.mintrocket.showcase.screens.authentication

import com.mintrocket.datacore.errorhandling.PhoneNotFoundException
import com.mintrocket.datacore.errorhandling.WrongCodeException
import com.mintrocket.modules.auth.core_ui.external.IAuthCodeDataSource

class AuthCodeDataSource : IAuthCodeDataSource {

    private val correctCode = "11111"
    private val registeredPhone = "9000000001"

    override suspend fun sendCode(phone: String) {
        if (phone != registeredPhone) throw PhoneNotFoundException(Exception())
    }

    override suspend fun checkCode(phone: String, code: String) {
        if (code != correctCode) throw WrongCodeException(Exception())
    }
}