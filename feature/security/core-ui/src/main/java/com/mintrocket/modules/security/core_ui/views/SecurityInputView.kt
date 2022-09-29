package com.mintrocket.modules.security.core_ui.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isInvisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.button.MaterialButton
import com.mintrocket.modules.security.core_ui.R
import com.mintrocket.modules.security.core_ui.databinding.ViewSecurityInputBinding

class SecurityInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding by viewBinding<ViewSecurityInputBinding>(attachToRoot = true)

    private val numButtons by lazy {
        with(binding) {
            listOf(
                btNum0, btNum1, btNum2, btNum3, btNum4, btNum5, btNum6, btNum7, btNum8, btNum9
            )
        }
    }

    private var pinDotsCount = 0
    private var currentPinValue = ""

    var title: String
        get() = binding.tvTitle.text?.toString().orEmpty()
        set(value) {
            binding.tvTitle.text = value
        }

    var biometricEnabled: Boolean = false
        set(value) {
            binding.btBiometric.isInvisible = !value
            field = value
        }

    var valueListener: ((value: String, filled: Boolean) -> Unit)? = null
    var biometricClickListener: (() -> Unit)? = null

    init {
        numButtons.forEach { button ->
            button.setOnClickListener(::onNumClick)
        }
        binding.btDelete.setOnClickListener { onDeleteClick() }
        binding.btBiometric.setOnClickListener { biometricClickListener?.invoke() }
        setDotsCount(pinDotsCount)
    }

    fun setDotsCount(count: Int) {
        pinDotsCount = count
        binding.pinDots.setDotsCount(pinDotsCount)
    }

    fun clearValue() {
        updatePin("")
    }

    private fun onNumClick(button: View) {
        val num = (button as MaterialButton).text?.toString().orEmpty()
        if (currentPinValue.length >= pinDotsCount) return
        updatePin(currentPinValue + num)
    }

    private fun onDeleteClick() {
        if (currentPinValue.isEmpty()) return
        updatePin(currentPinValue.take(currentPinValue.length - 1))
    }

    private fun updatePin(targetPin: String) {
        currentPinValue = targetPin.take(pinDotsCount)
        binding.pinDots.setFilledDots(currentPinValue.length)
        valueListener?.invoke(currentPinValue, currentPinValue.length >= pinDotsCount)
    }
}