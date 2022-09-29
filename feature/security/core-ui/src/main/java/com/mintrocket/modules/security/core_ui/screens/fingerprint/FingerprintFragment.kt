package com.mintrocket.modules.security.core_ui.screens.fingerprint

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.modules.security.core_ui.R
import com.mintrocket.modules.security.core_ui.databinding.FragmentSecurityFingerprintBinding
import com.mintrocket.uicore.attachBackPressed
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FingerprintFragment : Fragment(R.layout.fragment_security_fingerprint) {

    companion object {
        fun newInstance() = FingerprintFragment()
    }

    private val viewModel by viewModel<FingerprintViewModel>()

    private val binding by viewBinding<FragmentSecurityFingerprintBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        attachBackPressed { viewModel.onBackPressed() }
        binding.toolbar.setNavigationOnClickListener { viewModel.onBackPressed() }
        binding.btAccept.setOnClickListener { viewModel.onAcceptClick() }
        binding.btCancel.setOnClickListener { viewModel.onCancelClick() }
    }
}