package com.mintrocket.modules.auth.core_ui.data.local

import com.mintrocket.modules.auth.core_ui.data.model.SentCode

interface SentCodeDataSource {

    suspend fun get(phone: String): SentCode?

    suspend fun save(sentCode: SentCode)

}