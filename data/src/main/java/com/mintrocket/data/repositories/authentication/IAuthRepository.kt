package com.mintrocket.data.repositories.authentication

interface IAuthRepository {

    suspend fun sendCode(phone: String)

    suspend fun checkCode(phone: String, code: String)

    suspend fun isUserAuthenticated(): Boolean

    suspend fun logout()
}