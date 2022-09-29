package com.mintrocket.modules.auth.core_ui.data.local

import android.content.Context
import com.mintrocket.modules.auth.core_ui.AuthFeatureConfig
import com.mintrocket.modules.auth.core_ui.data.model.SentCode
import com.mintrocket.modules.auth.core_ui.data.model.local.SentCodeData
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

class SentCodeDataSourceImpl(
    private val context: Context,
    private val moshi: Moshi
) : SentCodeDataSource {

    companion object {
        private const val PREFERENCE_NAME = "sent_code_prefs"
        private const val KEY_SENT_CODES = "key_sent_codes"
    }

    private val codesAdapter by lazy {
        val type = Types.newParameterizedType(
            Set::class.java,
            SentCodeData::class.java
        )
        moshi.adapter<Set<SentCodeData>>(type)
    }

    private val sharedPreferences by lazy {
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    override suspend fun get(phone: String): SentCode? {
        val codes = getAllCodes()
        return codes[phone]
    }

    override suspend fun save(sentCode: SentCode) {
        val codes = getAllCodes().toMutableMap()
        codes[sentCode.phone] = sentCode
        saveCodes(codes)
    }

    private fun getAllCodes(): Map<String, SentCode> {
        val codesSet = sharedPreferences
            .getString(KEY_SENT_CODES, null)
            ?.let { codesAdapter.fromJson(it) }
            ?: return emptyMap()

        return codesSet
            .map { it.toDomain() }
            .associateBy { it.phone }
    }

    private fun saveCodes(codes: Map<String, SentCode>) {
        val codesSet = codes.values.map { it.toData() }.toSet()
        val jsonSrc = codesAdapter.toJson(codesSet)
        sharedPreferences.edit().putString(KEY_SENT_CODES, jsonSrc).apply()
    }

    private fun SentCodeData.toDomain() = SentCode(phone, sentAt)

    private fun SentCode.toData() = SentCodeData(phone, sentAt)

}