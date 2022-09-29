package com.mintrocket.modules.security.data

import android.content.Context
import com.mintrocket.modules.security.SecurityFeatureConfig

class SecurityDataSourceImpl(
    private val config: SecurityFeatureConfig,
    context: Context
) : SecurityDataSource {

    private val sharedPreferences = context.getSharedPreferences(
        config.preferencesName,
        Context.MODE_PRIVATE
    )

    override suspend fun getCode(): String? {
        return sharedPreferences.getString(config.pinCodeKey, null)
    }

    override suspend fun setCode(value: String) {
        sharedPreferences.edit().putString(config.pinCodeKey, value).apply()
    }

    override suspend fun deleteCode() {
        sharedPreferences.edit().remove(config.pinCodeKey).apply()
    }


    override suspend fun getBiometricEnabled(): Boolean {
        return sharedPreferences.getBoolean(config.biometricKey, false)
    }

    override suspend fun setBiometricEnabled(value: Boolean) {
        sharedPreferences.edit().putBoolean(config.biometricKey, value).apply()
    }

    override suspend fun deleteBiometric() {
        sharedPreferences.edit().remove(config.biometricKey).apply()
    }
}