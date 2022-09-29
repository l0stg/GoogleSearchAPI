package com.mintrocket.modules.security.data

interface SecurityDataSource {
    suspend fun getCode(): String?
    suspend fun setCode(value: String)
    suspend fun deleteCode()

    suspend fun getBiometricEnabled(): Boolean
    suspend fun setBiometricEnabled(value: Boolean)
    suspend fun deleteBiometric()
}