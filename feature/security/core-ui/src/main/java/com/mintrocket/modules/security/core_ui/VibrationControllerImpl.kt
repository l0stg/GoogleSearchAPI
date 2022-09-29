package com.mintrocket.modules.security.core_ui

import android.content.Context
import com.mintrocket.modules.security.core_ui.utils.VibrationHelper
import com.mintrocket.modules.security.external.VibrationController

class VibrationControllerImpl(
    private val context: Context
) : VibrationController {

    override fun vibrateWrongCode() {
        VibrationHelper.oneShot(context)
    }
}