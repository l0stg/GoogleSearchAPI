package com.mintrocket.modules.security.core_ui.screens.settings

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.modules.security.core_ui.R
import com.mintrocket.modules.security.core_ui.databinding.FragmentSecuritySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class SecuritySettingsFragment : Fragment(R.layout.fragment_security_settings) {

    companion object {
        fun newInstance() = SecuritySettingsFragment()
    }

    private val viewModel by viewModel<SecuritySettingsViewModel>()

    private val binding by viewBinding<FragmentSecuritySettingsBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener { viewModel.onBackPressed() }

        viewModel.biometricEnabled().observe(viewLifecycleOwner) {
            binding.btSwitchBiometric.isVisible = it
        }
        viewModel.pinCodeState().observe(viewLifecycleOwner) {
            with(binding.btSwitchPinCode) {
                setOnCheckedChangeListener(null)
                isChecked = it
                setPendingCheckedListener(viewModel::onPinCodeClick)
            }
        }
        viewModel.biometricState().observe(viewLifecycleOwner) {
            with(binding.btSwitchBiometric) {
                setOnCheckedChangeListener(null)
                isChecked = it
                setPendingCheckedListener(viewModel::onBiometricClick)
            }
        }
    }

    private fun CompoundButton.setPendingCheckedListener(action: (Boolean) -> Unit): CompoundButton.OnCheckedChangeListener {
        val listener = object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(button: CompoundButton, checked: Boolean) {
                button.setOnCheckedChangeListener(null)
                button.isChecked = !checked
                action.invoke(checked)
                button.setOnCheckedChangeListener(this)
            }
        }
        setOnCheckedChangeListener(listener)
        return listener
    }
}