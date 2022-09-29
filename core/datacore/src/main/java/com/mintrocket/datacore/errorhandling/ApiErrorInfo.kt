package com.mintrocket.datacore.errorhandling

data class ApiErrorInfo(
    val message: String,
    val textCode: String?,
    val code: Int,
)