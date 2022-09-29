package com.mintrocket.security.example.di.security

import androidx.appcompat.app.AppCompatActivity
import com.mintrocket.modules.security.SecurityFeatureConfig
import com.mintrocket.modules.security.external.*
import com.mintrocket.modules.security.screens.PinCheckResult
import com.mintrocket.navigation.ScreenResultsBuffer
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

private const val DEFAULT_SECURITY_TIMEOUT_SEC = 5L
private const val DEFAULT_SECURITY_PIN_DOTS = 6

val securityFeatureModule = module {
    single {
        SecurityFeatureConfig(
            timeoutInMillis = TimeUnit.SECONDS.toMillis(DEFAULT_SECURITY_TIMEOUT_SEC),
            pinDotsCount = DEFAULT_SECURITY_PIN_DOTS
        )
    }

    single<SecurityLogoutUseCase> { SecurityLogoutUseCaseImpl(get()) }
    single<SecurityErrorHandler> { SecurityErrorHandlerImpl(get()) }

    /* Routers */
    factory<FingerprintRouter> {
        FingerprintRouterImpl(
            navigator = get()
        )
    }
    factory<PinBarrierRouter> { (activity: AppCompatActivity) ->
        val config = get<SecurityFeatureConfig>()
        val resultsFlow =
            get<ScreenResultsBuffer>().getResultFlow<PinCheckResult>(config.launchBarrierTag)
        PinBarrierRouterImpl(
            activity = activity,
            resultsFlow = resultsFlow
        )
    }
    factory<PinCheckRouter> {
        val config = get<SecurityFeatureConfig>()
        val resultsFlow =
            get<ScreenResultsBuffer>().getResultFlow<PinCheckResult>(config.settingsCheckTag)
        PinCheckRouterImpl(
            navigator = get(),
            resultsFlow = resultsFlow
        )
    }
    factory<PinCreateRouter> {
        PinCreateRouterImpl(
            navigator = get()
        )
    }
    factory<SecurityLaunchRouter> { SecurityLaunchRouterImpl(get()) }
    factory<SecuritySettingsRouter> {
        SecuritySettingsRouterImpl(
            navigator = get()
        )
    }
}