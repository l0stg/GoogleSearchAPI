package com.mintrocket.modules.auth.core_ui.screens.login_pass

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.modules.auth.core_ui.AuthFeatureConfig
import com.mintrocket.modules.auth.core_ui.AuthTypeConfig
import com.mintrocket.modules.auth.core_ui.R
import com.mintrocket.modules.auth.core_ui.databinding.FragmentAuthLoginPassBinding
import com.mintrocket.modules.auth.core_ui.extensions.setupLicenseText
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.ldralighieri.corbind.widget.textChanges

class LoginPassAuthFragment : Fragment(R.layout.fragment_auth_login_pass) {

    private val config by inject<AuthFeatureConfig>()
    private val typeConfig by lazy {
        config.authTypeConfig as AuthTypeConfig.LoginAndPass
    }
    private val viewModel by viewModel<LoginPassAuthViewModel>()
    private val binding by viewBinding<FragmentAuthLoginPassBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initObservers()

        view.setOnApplyWindowInsetsListener { v, insets ->
            v.updatePadding(bottom = insets.systemWindowInsetBottom)
            insets
        }
    }

    private fun initViews() {
        setupLicenseText(
            binding.tvAgreements,
            config
        ) { viewModel.onLegalClicked(it) }

        binding.tilLogin.hint = getString(typeConfig.loginHint)
        binding.tivLogin.inputType = typeConfig.inputType
        binding.tivLogin.textChanges()
            .onEach {
                viewModel.onInputChanged(
                    it.toString(),
                    binding.tivPassword.text?.toString().orEmpty()
                )
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.tivPassword.textChanges()
            .onEach {
                viewModel.onInputChanged(
                    binding.tivLogin.text?.toString().orEmpty(),
                    it.toString()
                )
            }.launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btSignIn.setOnClickListener {
            viewModel.auth(
                binding.tivLogin.text?.toString().orEmpty(),
                binding.tivPassword.text?.toString().orEmpty()
            )
        }
    }

    private fun initObservers() {
        with(viewModel) {
            progress.observe(viewLifecycleOwner) {
                binding.progressSignIn.isVisible = it
            }

            buttonState.observe(viewLifecycleOwner) {
                binding.btSignIn.isEnabled = it
            }

            fieldsState.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is LoginAndPassState.Empty -> {
                        binding.tilLogin.isErrorEnabled = false
                        binding.tilPassword.isErrorEnabled = false
                    }
                    is LoginAndPassState.WrongCredentials -> {
                        binding.tilLogin.error = " "
                        binding.tilLogin.isErrorEnabled = true
                        binding.tilPassword.error = getString(state.errorMessage)
                        binding.tilPassword.isErrorEnabled = true
                    }
                    is LoginAndPassState.InvalidLoginFormat -> {
                        binding.tilLogin.error = getString(state.errorMessage)
                        binding.tilLogin.isErrorEnabled = true
                        binding.tilPassword.isErrorEnabled = false
                    }
                }
            }
        }
    }
}