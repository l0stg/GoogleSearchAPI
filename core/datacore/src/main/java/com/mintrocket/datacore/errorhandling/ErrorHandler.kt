package com.mintrocket.datacore.errorhandling

import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.net.UnknownHostException

class ErrorHandler : IErrorHandler {

    override fun parseThrowable(throwable: Throwable): ApiException {
        val apiError = if (throwable is HttpException) {
            val message = parseErrorMessage(throwable)
            convertMessageToApiError(message, throwable)
        } else null

        return apiError ?: when (throwable) {
            is UnknownHostException -> NoInternetException(throwable)
            else -> UnknownException(throwable)
        }
    }

    @Suppress("ReturnCount")
    private fun parseErrorMessage(throwable: HttpException): ApiErrorInfo? {
        val response = throwable.response() ?: return null
        val responseBody = throwable.response()?.errorBody()?.string() ?: return null
        return try {
            Timber.d("TESTING response $responseBody")
            val json = JSONObject(responseBody)
            val errorObject = json.getJSONArray("errors")
                .getJSONObject(0)
            val detail = errorObject.getString("detail")
            val textCode = errorObject.optString("code")
            ApiErrorInfo(detail, textCode, response.code())
        } catch (ex: Exception) {
            Timber.e(ex)
            null
        }
    }

    private fun convertMessageToApiError(
        info: ApiErrorInfo?,
        reason: HttpException
    ): ApiException? {
        return when (info?.textCode) {
            NetworkErrorCode.PHONE_NOT_FOUND.code -> PhoneNotFoundException(reason)
            NetworkErrorCode.INCORRECT_CONFIRM_CODE.code -> WrongCodeException(reason)
            NetworkErrorCode.PHONE_ALREADY_REGISTERED.code -> PhoneAlreadyRegisteredException(reason)
            else -> UnknownApiException(reason)
        }
    }
}