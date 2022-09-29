package com.mintrocket.modules.auth.core_ui.external

interface IAuthCodeDataSource {

    suspend fun sendCode(phone: String)

    suspend fun checkCode(phone: String, code: String)
}