package com.mintrocket.mobile

class InputValidator {

    companion object{
        private const val MIN_PASSWORD_LENGTH = 7
    }

    private val passwordRegex =
        "^(?=.*[0-9])(?=.*[a-z])(?=\\S+\$).{8,}$".toRegex(RegexOption.IGNORE_CASE)
    private val emailRegex =
        "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$".toRegex(RegexOption.IGNORE_CASE)

    fun isValidRegistrationPassword(pass: String) = passwordRegex.matches(pass)

    fun isValidAuthPassword(pass: String) = pass.length > MIN_PASSWORD_LENGTH

    fun isValidEmail(email: String) = emailRegex.matches(email)

    fun isValidPartialPassRepeat(pass: String, repeat: String): Boolean {
        return when {
            pass == repeat -> true
            repeat.length >= pass.length -> false
            pass.startsWith(repeat) -> true
            else -> false
        }
    }
}