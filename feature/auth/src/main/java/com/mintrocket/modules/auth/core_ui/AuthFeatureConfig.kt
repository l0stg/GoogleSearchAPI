package com.mintrocket.modules.auth.core_ui

import androidx.annotation.StringRes
import com.mintrocket.modules.auth.core_ui.external.IAuthRouter
import com.mintrocket.modules.auth.core_ui.screens.login_pass.LoginPassAuthFragment
import com.mintrocket.modules.auth.core_ui.screens.phone.EnterPhoneFragment
import java.util.concurrent.TimeUnit

data class AuthFeatureConfig(
    val authTypeConfig: AuthTypeConfig,
    @StringRes val legalTextRes: Int? = null,
    val legals: List<AuthLegal> = emptyList()
)

sealed class AuthTypeConfig {
    data class PhoneAndCode(
        val phoneMask: String = "+7 [000] [000]-[00]-[00]",
        val phoneHint: String = "+7",
        val codeInMessageRegex: Regex = Regex("([\\d]{5})\$"),
        val codeLength: Int = 5,
        val resendCodeDelayMillis: Long = TimeUnit.MINUTES.toMillis(1),
        val confirmCodeWithButton: Boolean = true,
    ) : AuthTypeConfig()

    data class LoginAndPass(
        @StringRes val loginHint: Int,
        val loginRegex: Regex?,
        val inputType: Int,
        @StringRes val loginRegexErrorMessage: Int,
        @StringRes val wrongCredsErrorMessage: Int,
        val minPassLength: Int = 6,
    ) : AuthTypeConfig()
}

class AuthFeature(
    private val config: AuthFeatureConfig,
    private val router: IAuthRouter
) {

    fun runAuthFlow() {
        router.openFragmentAsHome {
            when (config.authTypeConfig) {
                is AuthTypeConfig.PhoneAndCode -> EnterPhoneFragment()
                is AuthTypeConfig.LoginAndPass -> LoginPassAuthFragment()
            }
        }
    }
}

data class AuthLegal(val id: String, @StringRes val nameRes: Int)