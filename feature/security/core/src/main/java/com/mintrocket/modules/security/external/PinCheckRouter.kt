package com.mintrocket.modules.security.external

import com.mintrocket.modules.security.screens.PinCheckResult

interface PinCheckRouter {
    fun exit(result: PinCheckResult)
    fun showWrongCode()
    fun showMsg(msg: String)
}