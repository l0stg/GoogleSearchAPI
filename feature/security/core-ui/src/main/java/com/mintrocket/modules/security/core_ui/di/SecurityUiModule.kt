package com.mintrocket.modules.security.core_ui.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.asFlow
import com.mintrocket.modules.security.SecurityFeatureConfig
import com.mintrocket.modules.security.biometric.BiometricController
import com.mintrocket.modules.security.biometric.BiometricRequest
import com.mintrocket.modules.security.biometric.BiometricControllerImpl
import com.mintrocket.modules.security.core_ui.R
import com.mintrocket.modules.security.core_ui.VibrationControllerImpl
import com.mintrocket.modules.security.core_ui.screens.barrier.PinBarrierViewModel
import com.mintrocket.modules.security.core_ui.screens.check.PinCheckViewModel
import com.mintrocket.modules.security.core_ui.screens.create.PinCreateViewModel
import com.mintrocket.modules.security.core_ui.screens.fingerprint.FingerprintViewModel
import com.mintrocket.modules.security.core_ui.screens.settings.SecuritySettingsViewModel
import com.mintrocket.modules.security.data.SecurityDataSource
import com.mintrocket.modules.security.data.SecurityDataSourceImpl
import com.mintrocket.modules.security.data.SecurityRepository
import com.mintrocket.modules.security.data.SecurityRepositoryImpl
import com.mintrocket.modules.security.external.PinBarrierRouter
import com.mintrocket.modules.security.external.SecuritySettingsRouter
import com.mintrocket.modules.security.external.VibrationController
import com.mintrocket.modules.security.launch.SecurityLaunchController
import com.mintrocket.modules.security.screens.PinCheckResult
import com.mintrocket.modules.security.screens.fingerprint.FingerprintScreenPart
import com.mintrocket.modules.security.screens.pin_barrier.PinBarrierScreenPart
import com.mintrocket.modules.security.screens.pin_check.PinCheckScreenPart
import com.mintrocket.modules.security.screens.pin_create.PinCreateScreenPart
import com.mintrocket.modules.security.screens.settings.SecuritySettingsScreenPart
import com.mintrocket.navigation.ScreenResultsBuffer
import kotlinx.coroutines.flow.mapNotNull
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val securityUiModule = module {

    single { SecurityFeatureConfig() }

    single {
        val context = get<Context>()
        BiometricRequest(
            title = context.getString(R.string.security_biometric_title),
            description = context.getString(R.string.security_biometric_desc),
            negativeButton = context.getString(android.R.string.cancel)
        )
    }

    single<SecurityDataSource> { SecurityDataSourceImpl(get(), get()) }
    single<SecurityRepository> { SecurityRepositoryImpl(get()) }

    single { BiometricControllerImpl() }
    single<BiometricController> { get<BiometricControllerImpl>() }

    single<VibrationController> {
        VibrationControllerImpl(get())
    }

    single {
        val config = get<SecurityFeatureConfig>()
        val resultsBuffer = get<ScreenResultsBuffer>()
            .getResultFlow<PinCheckResult>(config.launchBarrierTag)
            .asFlow()
            .mapNotNull { it.content() }
        SecurityLaunchController(get(), get(), get(), resultsBuffer, get())
    }

    /* ScreenParts */
    factory {
        FingerprintScreenPart(
            securityRepository = get(),
            biometricController = get(),
            vibrationController = get(),
            router = get(),
            biometricRequest = get()
        )
    }
    factory { (router: PinBarrierRouter) ->
        PinBarrierScreenPart(
            securityRepository = get(),
            biometricController = get(),
            vibrationController = get(),
            logoutUseCase = get(),
            router = router,
            biometricRequest = get()
        )
    }
    factory {
        PinCheckScreenPart(
            securityRepository = get(),
            biometricController = get(),
            vibrationController = get(),
            router = get(),
            biometricRequest = get()
        )
    }
    factory {
        PinCreateScreenPart(
            biometricController = get(),
            vibrationController = get(),
            securityRepository = get(),
            router = get()
        )
    }
    factory {
        SecuritySettingsScreenPart(
            securityRepository = get(),
            biometricController = get(),
            vibrationController = get(),
            router = get(),
            biometricRequest = get()
        )
    }

    /* ViewModels */
    viewModel {
        FingerprintViewModel(
            screenPart = get(),
            errorHandler = get()
        )
    }
    viewModel { (activity: FragmentActivity) ->
        val router = get<PinBarrierRouter> { parametersOf(activity) }
        PinBarrierViewModel(
            screenPart = get { parametersOf(router) },
            errorHandler = get()
        )
    }
    viewModel {
        PinCheckViewModel(
            screenPart = get(),
            errorHandler = get()
        )
    }
    viewModel {
        PinCreateViewModel(
            screenPart = get(),
            errorHandler = get()
        )
    }
    viewModel {
        val config = get<SecurityFeatureConfig>()
        val resultsBuffer = get<ScreenResultsBuffer>()
            .getResultFlow<PinCheckResult>(config.settingsCheckTag)
            .asFlow()
            .mapNotNull { it.content() }

        SecuritySettingsViewModel(
            screenPart = get(),
            errorHandler = get(),
            resultsBuffer = resultsBuffer
        )
    }
}