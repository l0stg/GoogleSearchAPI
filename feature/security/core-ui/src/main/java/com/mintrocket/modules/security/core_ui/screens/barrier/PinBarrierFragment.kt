package com.mintrocket.modules.security.core_ui.screens.barrier

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.modules.security.SecurityFeatureConfig
import com.mintrocket.modules.security.core_ui.R
import com.mintrocket.modules.security.core_ui.databinding.FragmentSecurityBarrierBinding
import com.mintrocket.uicore.attachBackPressed
import com.mintrocket.uicore.observeEvent
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PinBarrierFragment : Fragment(R.layout.fragment_security_barrier) {

    companion object {
        fun newInstance() = PinBarrierFragment()
    }

    private val securityConfig by inject<SecurityFeatureConfig>()

    private val viewModel by viewModel<PinBarrierViewModel> {
        parametersOf(requireActivity())
    }

    private val binding by viewBinding<FragmentSecurityBarrierBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachBackPressed { viewModel.onBackPressed() }
        binding.btLogout.setOnClickListener { viewModel.onLogoutClick() }
        with(binding.securityInput) {
            setDotsCount(securityConfig.pinDotsCount)
            title = getString(R.string.security_check_title)
            valueListener = viewModel::onPinChanged
            biometricClickListener = { viewModel.requireBiometric() }
        }

        if (savedInstanceState == null) {
            viewModel.requireBiometric()
        }

        viewModel.clearAction().observeEvent(viewLifecycleOwner) {
            binding.securityInput.clearValue()
        }
        viewModel.biometricEnabled().observe(viewLifecycleOwner) { enabled ->
            binding.securityInput.biometricEnabled = enabled
        }
    }
}