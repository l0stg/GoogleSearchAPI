package com.mintrocket.modules.security.external

import com.mintrocket.modules.security.screens.PinCheckResult

interface PinBarrierRouter {
    fun exit(result: PinCheckResult)
    fun showWrongCode()
    fun showMsg(msg: String)
}