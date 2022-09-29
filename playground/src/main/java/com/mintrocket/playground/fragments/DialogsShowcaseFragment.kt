package com.mintrocket.playground.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.mintrocket.playground.R
import com.mintrocket.playground.databinding.FragmentDialogsShowcaseBinding
import com.mintrocket.uicore.onClick

class DialogsShowcaseFragment : Fragment(R.layout.fragment_dialogs_showcase) {

    private val binding by viewBinding<FragmentDialogsShowcaseBinding>()

    @Suppress("LongMethod", "MagicNumber")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btDialog.onClick {
            Dialog(requireContext()).apply {
                setContentView(R.layout.dialog_test_content)
            }.show()
        }

        binding.btDialogCompat.onClick {
            AppCompatDialog(requireContext()).apply {
                setContentView(R.layout.dialog_test_content)
            }.show()
        }

        binding.btDialogDate.onClick {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DatePickerDialog(
                    requireContext(),
                    { view, year, month, dayOfMonth -> },
                    2020,
                    1,
                    29
                ).show()
            }
        }

        binding.btDialogTime.onClick {
            TimePickerDialog(
                requireContext(),
                { view, hourOfDay, minute -> },
                12,
                45,
                true
            ).show()
        }

        binding.btDialogAlert.onClick {
            AlertDialog.Builder(requireContext())
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Positive", null)
                .setNegativeButton("Negative", null)
                .show()
        }
        binding.btDialogCompatAlert.onClick {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Positive", null)
                .setNegativeButton("Negative", null)
                .show()
        }
        binding.btDialogMtAlert.onClick {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Title")
                .setMessage("Message")
                .setPositiveButton("Positive", null)
                .setNegativeButton("Negative", null)
                .show()
        }
        binding.btDialogMtCalendar.onClick {
            MaterialDatePicker.Builder.datePicker()
                .build()
                .show(childFragmentManager, "mtdate")
        }
        binding.btDialogMtTime.onClick {
            MaterialTimePicker.Builder()
                .build()
                .show(childFragmentManager, "mttime")
        }
        binding.btDialogBottomSheet.onClick {
            BottomSheetDialog(requireContext()).apply {
                setContentView(R.layout.dialog_test_content)
            }.show()
        }
    }
}