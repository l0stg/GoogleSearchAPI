package com.mintrocket.security.example.screens.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.datacore.errorhandling.IErrorHandler
import com.mintrocket.datacore.utils.toContainer
import com.mintrocket.modules.security.biometric.BiometricControllerImpl
import com.mintrocket.navigation.containers.ContainerActivity
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.navigation.navigator.holder.AppNavigatorHolder
import com.mintrocket.navigation.screens.BaseAppScreen
import com.mintrocket.security.example.R
import com.mintrocket.security.example.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class MainActivity : ContainerActivity() {

    companion object {

        fun getIntent(context: Context, newTask: Boolean = false) =
            Intent(context, MainActivity::class.java).apply {
                if (newTask) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            }
    }

    private val viewModel by viewModel<MainViewModel>()

    private val biometricController by inject<BiometricControllerImpl>()

    private val binding by viewBinding<ActivityMainBinding>()

    override val navigator by inject<ApplicationNavigator>()

    override val navigatorHolder by lazy {
        AppNavigatorHolder(
            this,
            this,
            containerId
        )
    }

    override val containerId: Int = R.id.fragmentContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        biometricController.attachTo(this)
        viewModel.initNavigation()
    }

    override fun onResume() {
        super.onResume()
        biometricController.attachTo(this)
    }

    override fun onPause() {
        biometricController.detach()
        super.onPause()
    }

    override fun getSnackBarContainer() = binding.content

    override fun handleError(throwable: Throwable, requiredAuthRedirect: Boolean): BaseAppScreen? {
        Timber.e(throwable)
        showToast(throwable.message.orEmpty().toContainer())
        return null
    }
}