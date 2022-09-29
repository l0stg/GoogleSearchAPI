package com.mintrocket.modules.security.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onStart

class SecurityRepositoryImpl(
    private val dataSource: SecurityDataSource,
) : SecurityRepository {

    private val pinCodeEnabledFlow = MutableStateFlow(false)

    private val biometricEnabledFlow = MutableStateFlow(false)

    override fun pinCodeEnabled(): Flow<Boolean> =
        pinCodeEnabledFlow.onStart { updatePinCode() }

    override fun biometricEnabled(): Flow<Boolean> =
        biometricEnabledFlow.onStart { updateBiometric() }

    override suspend fun saveCode(code: String) {
        dataSource.setCode(code)
        updatePinCode()
    }

    override suspend fun checkCode(code: String): Boolean {
        return dataSource.getCode() == code
    }

    override suspend fun setBiometricEnabled(enabled: Boolean) {
        dataSource.setBiometricEnabled(enabled)
        updateBiometric()
    }

    override suspend fun clear() {
        dataSource.deleteCode()
        dataSource.deleteBiometric()
        updatePinCode()
        updateBiometric()
    }

    private suspend fun updatePinCode() {
        pinCodeEnabledFlow.value = dataSource.getCode() != null
    }

    private suspend fun updateBiometric() {
        biometricEnabledFlow.value = dataSource.getBiometricEnabled()
    }
}