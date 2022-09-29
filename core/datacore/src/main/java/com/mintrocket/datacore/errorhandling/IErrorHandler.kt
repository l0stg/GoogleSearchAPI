package com.mintrocket.datacore.errorhandling

interface IErrorHandler {

    fun parseThrowable(throwable: Throwable): ApiException
}