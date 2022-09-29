package com.mintrocket.modules.auth.core_ui.di


import com.mintrocket.modules.auth.core_ui.AuthFeature
import com.mintrocket.modules.auth.core_ui.AuthFeatureConfig
import com.mintrocket.modules.auth.core_ui.AuthTypeConfig
import com.mintrocket.modules.auth.core_ui.data.local.SentCodeDataSource
import com.mintrocket.modules.auth.core_ui.data.local.SentCodeDataSourceImpl
import com.mintrocket.modules.auth.core_ui.data.repository.AuthCodeRepository
import com.mintrocket.modules.auth.core_ui.data.repository.IAuthCodeRepository
import com.mintrocket.modules.auth.core_ui.screens.code.EnterCodeViewModel
import com.mintrocket.modules.auth.core_ui.screens.login_pass.LoginPassAuthViewModel
import com.mintrocket.modules.auth.core_ui.screens.phone.EnterPhoneViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authFeatureModule = module {
    single<SentCodeDataSource> {
        SentCodeDataSourceImpl(
            context = get(),
            moshi = get()
        )
    }

    single<IAuthCodeRepository> {
        AuthCodeRepository(
            config = get(),
            authDataSource = get(),
            sentCodeDataSource = get()
        )
    }

    factory {
        AuthFeature(
            config = get(),
            router = get()
        )
    }

    factory {
        get<AuthFeatureConfig>()
            .authTypeConfig as AuthTypeConfig.PhoneAndCode
    }

    factory {
        get<AuthFeatureConfig>()
            .authTypeConfig as AuthTypeConfig.LoginAndPass
    }

    viewModel {
        EnterPhoneViewModel(
            router = get(),
            errorHandler = get(),
            authCodeRepository = get()
        )
    }

    viewModel { (phone: String) ->
        EnterCodeViewModel(
            phone = phone,
            config = get(),
            router = get(),
            errorHandler = get(),
            authCodeRepository = get()
        )
    }

    viewModel {
        LoginPassAuthViewModel(
            config = get(),
            router = get(),
            errorHandler = get(),
            dataSource = get()
        )
    }
}