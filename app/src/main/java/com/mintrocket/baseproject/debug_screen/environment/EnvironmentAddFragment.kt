package com.mintrocket.baseproject.debug_screen.environment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mintrocket.baseproject.R
import com.mintrocket.baseproject.databinding.DialogDebugEnvironmentAddBinding
import com.mintrocket.baseproject.debug_screen.environment.data.EnvironmentEntry
import com.mintrocket.uicore.extra
import com.mintrocket.uicore.observeEvent
import com.mintrocket.uicore.onClick
import com.mintrocket.uicore.withArgs
import org.koin.androidx.viewmodel.ext.android.viewModel

class EnvironmentAddFragment : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_ENTRY = "arg_entry"

        fun newInstance(entry: EnvironmentEntry?) = EnvironmentAddFragment().withArgs(
            ARG_ENTRY to entry
        )
    }

    private val binding by viewBinding<DialogDebugEnvironmentAddBinding>()
    private val viewModel by viewModel<EnvironmentAddViewModel>()

    private val argEntry by extra<EnvironmentEntry>(ARG_ENTRY)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_debug_environment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDialog()
        initView()
        initObservers()
    }

    private fun initDialog() {
        requireDialog().apply {
            setCancelable(true)
            setCanceledOnTouchOutside(false)
            (this as BottomSheetDialog).behavior.apply {
                isHideable = false
            }
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    private fun initView() {
        argEntry?.also { entry ->
            binding.etName.setText(entry.name)
            binding.etUrl.setText(entry.url)
        }

        binding.btSave.onClick {
            viewModel.onSaveClick(
                argEntry?.id,
                binding.etName.text?.toString().orEmpty(),
                binding.etUrl.text?.toString().orEmpty()
            )
        }

        binding.btCancel.onClick {
            viewModel.onCloseClick()
        }
    }

    private fun initObservers() {
        viewModel.closeEvent.observeEvent(viewLifecycleOwner) {
            dismiss()
        }

        viewModel.errorEvent.observeEvent(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }
}