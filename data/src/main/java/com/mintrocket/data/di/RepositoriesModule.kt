package com.mintrocket.data.di

import com.mintrocket.data.feature_toggling.repository.remote.RemoteFeaturesRepository
import com.mintrocket.data.feature_toggling.repository.remote.RemoteFeaturesRepositoryImpl
import com.mintrocket.data.repositories.authentication.AuthRepository
import com.mintrocket.data.repositories.authentication.IAuthRepository
import com.mintrocket.data.repositories.content.ContentRepository
import com.mintrocket.data.repositories.content.ContentRepositoryImpl
import org.koin.dsl.module

val repositoriesModule = module {

    single<ContentRepository> { ContentRepositoryImpl(get()) }

    single<IAuthRepository> { AuthRepository(get(), get()) }


    single<RemoteFeaturesRepository> {
        RemoteFeaturesRepositoryImpl(get())
    }
}