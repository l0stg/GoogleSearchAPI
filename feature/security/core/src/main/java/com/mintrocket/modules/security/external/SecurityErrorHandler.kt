package com.mintrocket.modules.security.external

interface SecurityErrorHandler {
    fun handleError(ex: Throwable)
}