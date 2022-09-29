package com.mintrocket.modules.security.data

import kotlinx.coroutines.flow.Flow


interface SecurityRepository {

    fun pinCodeEnabled(): Flow<Boolean>

    fun biometricEnabled(): Flow<Boolean>

    suspend fun saveCode(code: String)

    suspend fun checkCode(code: String): Boolean

    suspend fun setBiometricEnabled(enabled: Boolean)

    suspend fun clear()
}