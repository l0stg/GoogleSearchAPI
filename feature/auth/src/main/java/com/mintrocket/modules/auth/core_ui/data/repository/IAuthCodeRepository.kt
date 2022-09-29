package com.mintrocket.modules.auth.core_ui.data.repository

import com.mintrocket.modules.auth.core_ui.data.model.SendCodeResult


internal interface IAuthCodeRepository {

    suspend fun checkCode(phone: String, code: String)

    suspend fun sendCode(phone: String): SendCodeResult
}