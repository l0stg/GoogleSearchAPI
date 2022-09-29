package com.mintrocket.modules.auth.core_ui.external

interface IAuthLoginPassDataSource {

    suspend fun auth(login: String, password: String)
}