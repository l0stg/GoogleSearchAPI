package com.mintrocket.modules.security.external

interface SecuritySettingsRouter {
    fun exit()
    fun openCreate()
    fun openCheck()
    fun showMsg(msg: String)
    fun showSuggestToBiometricDialog(action: () -> Unit)
}