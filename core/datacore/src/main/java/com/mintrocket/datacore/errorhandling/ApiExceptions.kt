package com.mintrocket.datacore.errorhandling


import com.mintrocket.datacore.R
import com.mintrocket.datacore.utils.TextContainer

enum class NetworkErrorCode(val code: String) {
    PHONE_NOT_FOUND("phone_not_found"),
    INCORRECT_CONFIRM_CODE("confirm_is_incorrect"),
    PHONE_ALREADY_REGISTERED("phone_already_registered"),
}

abstract class ApiException(
    messageContainer: TextContainer,
    titleContainer: TextContainer,
    val reason: Throwable
) :
    ExceptionWithMessage(messageContainer, titleContainer)

class NoInternetException(reason: Throwable) :
    ApiException(
        TextContainer.ResContainer(R.string.data_core_no_internet),
        TextContainer.ResContainer(R.string.data_core_common_error_title),
        reason
    )

class UnknownApiException(reason: Throwable) :
    ApiException(
        TextContainer.ResContainer(R.string.data_core_network_error),
        TextContainer.ResContainer(R.string.data_core_common_error_title),
        reason
    )

class UnknownException(reason: Throwable) :
    ApiException(
        TextContainer.ResContainer(R.string.data_core_unknown_exception),
        TextContainer.ResContainer(R.string.data_core_common_error_title),
        reason
    )

class WrongCodeException(reason: Throwable) :
    ApiException(
        TextContainer.ResContainer(R.string.data_core_wrong_code),
        TextContainer.ResContainer(R.string.data_core_common_error_title),
        reason,
    )

class WrongCredentialsException(reason: Throwable) :
    ApiException(
        TextContainer.ResContainer(R.string.data_core_wrong_credentials),
        TextContainer.ResContainer(R.string.data_core_common_error_title),
        reason
    )

class PhoneNotFoundException(reason: Throwable) :
    ApiException(
        TextContainer.ResContainer(R.string.data_phone_not_registered),
        TextContainer.ResContainer(R.string.data_core_common_error_title),
        reason,
    )

class PhoneAlreadyRegisteredException(reason: Throwable) :
    ApiException(
        TextContainer.ResContainer(R.string.data_core_phone_registered_error),
        TextContainer.ResContainer(R.string.data_core_common_error_title),
        reason,
    )

class AuthorizationRequiredException(reason: Throwable) :
    ApiException(
        TextContainer.ResContainer(R.string.data_core_auth_required_error),
        TextContainer.ResContainer(R.string.data_core_common_error_title),
        reason,
    )