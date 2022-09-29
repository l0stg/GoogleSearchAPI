package com.mintrocket.modules.security.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class BiometricControllerImpl : BiometricController {

    private var activity: FragmentActivity? = null

    fun attachTo(activity: FragmentActivity) {
        this.activity = activity
    }

    fun detach() {
        activity = null
    }

    override suspend fun isSupported() = activity
        ?.let { BiometricManager.from(it) }
        ?.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS

    override suspend fun authenticate(request: BiometricRequest): BiometricResult {
        return suspendCancellableCoroutine { continuation ->
            val callback = object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    if (!continuation.isActive) return
                    continuation.resume(BiometricResult.Success)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    if (!continuation.isActive) return
                    val result = when (errorCode) {
                        BiometricPrompt.ERROR_USER_CANCELED,
                        BiometricPrompt.ERROR_NEGATIVE_BUTTON -> BiometricResult.Cancel
                        else -> BiometricResult.Error(errString.toString())
                    }

                    continuation.resume(result)
                }

                override fun onAuthenticationFailed() {
                    // Игнорим, т.к. вызывается несколько раз и на конечный результат не влияет
                }
            }
            val activity = this.activity
            if (activity == null) {
                continuation.resume(BiometricResult.Error("Not Ready"))
            } else {
                val executor = ContextCompat.getMainExecutor(activity)
                val biometricPrompt = BiometricPrompt(activity, executor, callback)
                continuation.invokeOnCancellation {
                    biometricPrompt.cancelAuthentication()
                }
                biometricPrompt.authenticate(request.toPrompt())
            }
        }
    }

    private fun BiometricRequest.toPrompt() = BiometricPrompt.PromptInfo.Builder()
        .setTitle(title)
        .setDescription(description)
        .setNegativeButtonText(negativeButton)
        .build()
}