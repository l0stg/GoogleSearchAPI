package com.mintrocket.modules.auth.core_ui.external

interface AuthErrorHandler {

    fun handleError(ex: Throwable)
}