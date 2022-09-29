package com.mintrocket.modules.security.core_ui.screens.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.modules.security.SecurityFeatureConfig
import com.mintrocket.modules.security.core_ui.R
import com.mintrocket.modules.security.core_ui.databinding.FragmentSecurityPinCreateBinding
import com.mintrocket.modules.security.screens.pin_create.PinCreateScreenPart
import com.mintrocket.uicore.attachBackPressed
import com.mintrocket.uicore.observeEvent
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PinCreateFragment : Fragment(R.layout.fragment_security_pin_create) {

    companion object {
        fun newInstance() = PinCreateFragment()
    }

    private val securityConfig by inject<SecurityFeatureConfig>()

    private val viewModel by viewModel<PinCreateViewModel>()

    private val binding by viewBinding<FragmentSecurityPinCreateBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachBackPressed { viewModel.onBackPressed() }
        binding.toolbar.setNavigationOnClickListener { viewModel.onBackPressed() }
        with(binding.securityInput) {
            setDotsCount(securityConfig.pinDotsCount)
            biometricEnabled = false
            valueListener = viewModel::onValueChanged
        }

        viewModel.titleState().observe(viewLifecycleOwner) {
            val titleRes = when (it) {
                PinCreateScreenPart.TitleState.CREATE -> R.string.security_create_title_create
                PinCreateScreenPart.TitleState.REPEAT -> R.string.security_create_title_repeat
            }
            binding.securityInput.title = getString(titleRes)
        }
        viewModel.clearAction().observeEvent(viewLifecycleOwner) {
            binding.securityInput.clearValue()
        }
    }
}