package com.mintrocket.modules.security.screens

sealed class PinCheckResult {
    object Success : PinCheckResult()
    class Cancel(val reason: CheckResultReason = CheckResultReason.CANCEL) : PinCheckResult()
}

enum class CheckResultReason {
    LOGOUT,
    CANCEL
}