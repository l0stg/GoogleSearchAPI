package com.mintrocket.modules.auth.core_ui.screens.code

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.mintrocket.modules.auth.core_ui.AuthFeatureConfig
import com.mintrocket.modules.auth.core_ui.AuthTypeConfig
import com.mintrocket.modules.auth.core_ui.R
import com.mintrocket.modules.auth.core_ui.databinding.FragmentAuthCodeBinding
import com.mintrocket.uicore.extraNotNull
import com.mintrocket.uicore.observeEvent
import com.mintrocket.uicore.showKeyboard
import com.mintrocket.uicore.withArgs
import com.redmadrobot.inputmask.helper.Mask
import com.redmadrobot.inputmask.model.CaretString
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import ru.ldralighieri.corbind.widget.textChanges
import timber.log.Timber

class EnterCodeFragment : Fragment(R.layout.fragment_auth_code) {

    companion object {
        private const val ARG_PHONE = "arg_phone"
        private const val SMS_CONSENT_REQUEST = 2234
        private const val MINUTE_DENOMINATOR = 60

        fun newInstance(phone: String) = EnterCodeFragment()
            .withArgs(ARG_PHONE to phone)
    }

    private val smsVerificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
                val extras = intent.extras
                val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                when (smsRetrieverStatus.statusCode) {
                    CommonStatusCodes.SUCCESS -> {
                        val consentIntent =
                            extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                        try {
                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST)
                        } catch (e: ActivityNotFoundException) {
                            Timber.e(e)
                        }
                    }
                    CommonStatusCodes.TIMEOUT -> {
                        // Time out occurred, handle the error.
                    }
                }
            }
        }
    }

    private val phone by extraNotNull<String>(ARG_PHONE)
    private val config by inject<AuthTypeConfig.PhoneAndCode>()

    private val viewModel by viewModel<EnterCodeViewModel> {
        parametersOf(phone)
    }

    private val binding by viewBinding<FragmentAuthCodeBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listenForCode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initSubscriptions()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopListeningCode()
    }

    private fun initView() {
        binding.toolbar.setNavigationOnClickListener { viewModel.onBackPressed() }
        binding.tvCodeSent.text = getString(R.string.auth_code_sent_format, formatPhone(phone))
        binding.etCode.setCellsCount(config.codeLength)
        binding.tvResendCode.setOnClickListener {
            binding.etCode.setText("")
            binding.etCode.drawWithError = false
            viewModel.onResendClicked()
        }
        binding.etCode.textChanges()
            .onEach { viewModel.onCodeChanged(it.toString()) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        binding.btConfirm.isVisible = config.confirmCodeWithButton
        binding.btConfirm.setOnClickListener {
            viewModel.onCheckCodeClicked(binding.etCode.text.toString())
        }
    }

    private fun initSubscriptions() {
        viewModel.progress().observe(viewLifecycleOwner) {
            binding.progressEnterCode.isVisible = it
            if (!it) binding.etCode.showKeyboard()
        }
        viewModel.timeToResend().observe(viewLifecycleOwner) { timeLeft ->
            if (timeLeft > 0) {
                binding.tvResendCode.isVisible = false
                binding.tvResendIn.isVisible = true
                val minutes = timeLeft / MINUTE_DENOMINATOR
                val seconds = timeLeft % MINUTE_DENOMINATOR
                val timeText = "%d:%02d".format(minutes, seconds)
                binding.tvResendIn.text = getString(R.string.auth_resend_code_format, timeText)
            } else {
                binding.tvResendCode.isVisible = true
                binding.tvResendIn.isVisible = false
            }
        }
        viewModel.incorrectCode().observe(viewLifecycleOwner) {
            binding.etCode.drawWithError = it
            binding.tvError.isVisible = it
        }
        viewModel.externalCode().observeEvent(viewLifecycleOwner) {
            binding.etCode.setText(it)
        }
        viewModel.sendCodeEnabled().observe(viewLifecycleOwner) {
            binding.btConfirm.isEnabled = it
        }
    }

    private fun listenForCode() {
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        requireActivity().registerReceiver(
            smsVerificationReceiver,
            intentFilter,
            SmsRetriever.SEND_PERMISSION,
            null
        )
        SmsRetriever.getClient(requireActivity()).startSmsUserConsent(null)
    }

    private fun stopListeningCode() {
        requireActivity().unregisterReceiver(smsVerificationReceiver)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SMS_CONSENT_REQUEST -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                    viewModel.onCodeMessageReceived(message)
                } else {
                    Timber.d("TESTING consent denied")
                }
            }
        }
    }

    private fun formatPhone(phone: String): String {
        val caretGravity = CaretString.CaretGravity.FORWARD(false)
        val caretString = CaretString(phone, 0, caretGravity)
        val mask = Mask.getOrCreate(config.phoneMask, listOf())
        val result = mask.apply(caretString)
        return result.formattedText.string
    }
}