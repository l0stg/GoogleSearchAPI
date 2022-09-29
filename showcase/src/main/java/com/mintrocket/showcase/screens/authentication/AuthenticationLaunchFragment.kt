package com.mintrocket.showcase.screens.authentication

import android.os.Bundle
import android.text.InputType
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.modules.auth.core_ui.*
import com.mintrocket.showcase.R
import com.mintrocket.showcase.databinding.FragmentAuthenticationLaunchBinding
import org.koin.android.ext.android.getKoin

class AuthenticationLaunchFragment : Fragment(R.layout.fragment_authentication_launch) {

    companion object {
        fun newInstance() = AuthenticationLaunchFragment()
    }

    private val binding by viewBinding<FragmentAuthenticationLaunchBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btPhoneAndCode.setOnClickListener {
            launchPhoneAuthentication()
        }

        binding.btLoginAndPass.setOnClickListener {
            launchLoginAndPassAuthentication()
        }
    }

    private fun launchPhoneAuthentication() {
        with(getKoin()) {
            declare(
                AuthFeatureConfig(
                    authTypeConfig = AuthTypeConfig.PhoneAndCode(
                        codeInMessageRegex = Regex("([\\d]{4})\$"),
                        codeLength = 5,
                        confirmCodeWithButton = false
                    ),


                    legalTextRes = R.string.auth_agreements_format,
                    legals = listOf(
                        AuthLegal("data processing", R.string.auth_data_processing),
                        AuthLegal("privacy policy", R.string.auth_privacy_policy)
                    ),
                ),
            )
            get<AuthFeature>().runAuthFlow()
        }
    }

    private fun launchLoginAndPassAuthentication() {
        with(getKoin()) {
            declare(
                AuthFeatureConfig(
                    authTypeConfig = AuthTypeConfig.LoginAndPass(
                        minPassLength = 6,
                        loginHint = R.string.auth_input_email_hint,
                        loginRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$".toRegex(RegexOption.IGNORE_CASE),
                        loginRegexErrorMessage = R.string.auth_input_correct_email,
                        wrongCredsErrorMessage = R.string.auth_login_or_pass_incorrect,
                        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                    ),
                    legalTextRes = R.string.auth_agreements_format,
                    legals = listOf(
                        AuthLegal("data processing", R.string.auth_data_processing),
                        AuthLegal("privacy policy", R.string.auth_privacy_policy)
                    ),
                ),
            )
            get<AuthFeature>().runAuthFlow()
        }
    }
}