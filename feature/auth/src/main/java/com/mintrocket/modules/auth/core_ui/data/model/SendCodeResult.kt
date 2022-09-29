package com.mintrocket.modules.auth.core_ui.data.model

sealed class SendCodeResult {
    object NotRegistered : SendCodeResult()
    data class Sent(val timeToResend: Long) : SendCodeResult()
}