package com.mintrocket.data.di

import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.mintrocket.data.api.ApiUrlProvider
import com.mintrocket.data.api.ApplicationApi
import com.mintrocket.data.api.FakeApi
import com.mintrocket.data.api.interceptor.AcceptInterceptor
import com.mintrocket.data.api.interceptor.DummyInterceptor
import com.mintrocket.data.repositories.authentication.AuthTokenDataSource
import com.mintrocket.data.repositories.authentication.AuthTokenInterceptor
import com.mintrocket.data.repositories.authentication.IAuthTokenDataSource
import com.mintrocket.datacore.AppBuildConfig
import com.mintrocket.datacore.errorhandling.ErrorHandler
import com.mintrocket.datacore.errorhandling.IErrorHandler
import com.squareup.moshi.Moshi
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

val AUTH_INTERCEPTOR_NAME = named("auth_interceptor")
val ACCEPT_INTERCEPTOR_NAME = named("accept_interceptor")
val LOGGING_INTERCEPTOR_NAME = named("logging_interceptor")
val CHUCKER_INTERCEPTOR_NAME = named("chucker_interceptor")

val REFRESH_RETROFIT_NAME = named("refresh_retrofit")
val REFRESH_TOKEN_API_NAME = named("refresh_token_api")
val REFRESH_TOKEN_CLIENT_NAME = named("refresh_token_client")

val httpModule = module {

    /* Common */
    single<IErrorHandler> {
        ErrorHandler()
    }

    single {
        Moshi.Builder()
            .build()
    }

    single<Converter.Factory> { MoshiConverterFactory.create(get()) }

    single<IAuthTokenDataSource> {
        AuthTokenDataSource(get(AUTH_DATA_PREFS))
    }


    /* Interceptors */
    single(LOGGING_INTERCEPTOR_NAME) {
        val appBuildConfig = get<AppBuildConfig>()
        if (appBuildConfig.isDebug) {
            HttpLoggingInterceptor { message -> Timber.i(message) }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            DummyInterceptor()
        }
    }

    single(CHUCKER_INTERCEPTOR_NAME) {
        val appBuildConfig = get<AppBuildConfig>()
        if (appBuildConfig.isDebug && appBuildConfig.isDebugScreenEnabled) {
            ChuckerInterceptor.Builder(get()).build()
        } else {
            DummyInterceptor()
        }
    }

    single(ACCEPT_INTERCEPTOR_NAME) {
        AcceptInterceptor()
    }

    single<Interceptor>(AUTH_INTERCEPTOR_NAME) {
        AuthTokenInterceptor(get(), get())
    }


    /* Main api */
    //todo remove this instances
    single<ApplicationApi> { FakeApi(get()) }
    single<ApplicationApi>(REFRESH_TOKEN_API_NAME) {
        FakeApi(get())
    }

    //todo uncomment and create api with the retrofit
    /*single<ApplicationApi> { get<Retrofit>().create(ApplicationApi::class.java) }
    single<ApplicationApi>(REFRESH_TOKEN_API_NAME) {
        get<Retrofit>(REFRESH_RETROFIT_NAME).create(ApplicationApi::class.java)
    }*/

    single {
        val urlProvider = get<ApiUrlProvider>()
        Retrofit.Builder()
            .baseUrl(urlProvider.getBaseUrl())
            .client(get())
            .addConverterFactory(get())
            .build()
    }

    single {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>(ACCEPT_INTERCEPTOR_NAME))
            .addInterceptor(get<Interceptor>(AUTH_INTERCEPTOR_NAME))
            .addInterceptor(get<Interceptor>(LOGGING_INTERCEPTOR_NAME))
            .addInterceptor(get<Interceptor>(CHUCKER_INTERCEPTOR_NAME))
            .authenticator(get<Authenticator>())
            .build()
    }


    /* Refresh token api */
    single(REFRESH_RETROFIT_NAME) {
        val urlProvider = get<ApiUrlProvider>()
        Retrofit.Builder()
            .baseUrl(urlProvider.getBaseUrl())
            .client(get(REFRESH_TOKEN_CLIENT_NAME))
            .addConverterFactory(get())
            .build()
    }

    single(REFRESH_TOKEN_CLIENT_NAME) {
        OkHttpClient.Builder()
            .addInterceptor(get<Interceptor>(ACCEPT_INTERCEPTOR_NAME))
            .addInterceptor(get<Interceptor>(LOGGING_INTERCEPTOR_NAME))
            .addInterceptor(get<Interceptor>(CHUCKER_INTERCEPTOR_NAME))
            .build()
    }
}