package com.mintrocket.modules.security.core_ui.screens.barrier

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.mintrocket.modules.security.biometric.BiometricControllerImpl
import com.mintrocket.modules.security.core_ui.R
import org.koin.android.ext.android.inject

class PinBarrierActivity : AppCompatActivity(R.layout.activity_security_barrier) {

    companion object {

        fun getIntent(context: Context) =
            Intent(context, PinBarrierActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private val biometricController by inject<BiometricControllerImpl>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        biometricController.attachTo(this)

        if (savedInstanceState == null) {
            val fragment = PinBarrierFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        biometricController.attachTo(this)
    }

    override fun onPause() {
        biometricController.detach()
        super.onPause()
    }
}