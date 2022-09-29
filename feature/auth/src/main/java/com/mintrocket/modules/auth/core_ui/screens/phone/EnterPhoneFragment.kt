package com.mintrocket.modules.auth.core_ui.screens.phone

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.modules.auth.core_ui.AuthFeatureConfig
import com.mintrocket.modules.auth.core_ui.AuthTypeConfig
import com.mintrocket.modules.auth.core_ui.R
import com.mintrocket.modules.auth.core_ui.databinding.FragmentAuthPhoneBinding
import com.mintrocket.modules.auth.core_ui.extensions.setupLicenseText
import com.redmadrobot.inputmask.MaskedTextChangedListener
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnterPhoneFragment : Fragment(R.layout.fragment_auth_phone) {

    private val config by inject<AuthFeatureConfig>()
    private val typeConfig by lazy {
        config.authTypeConfig as AuthTypeConfig.PhoneAndCode
    }
    private val viewModel by viewModel<EnterPhoneViewModel>()

    private val binding by viewBinding<FragmentAuthPhoneBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initSubscriptions()
    }

    private fun initView() {
        MaskedTextChangedListener.installOn(
            binding.tivPhone,
            typeConfig.phoneMask,
            object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    viewModel.onInputChanged(extractedValue, maskFilled)
                }
            })

        binding.btSignIn.setOnClickListener { viewModel.onSendCodeClicked() }
        setupLicenseText(
            binding.tvAgreements,
            config
        ) { viewModel.onLegalClick(it) }
    }

    private fun initSubscriptions() {
        viewModel.phoneFilled().observe(viewLifecycleOwner) {
            binding.btSignIn.isEnabled = it
        }
        viewModel.progress().observe(viewLifecycleOwner) {
            binding.progressSignIn.isVisible = it
        }
    }
}