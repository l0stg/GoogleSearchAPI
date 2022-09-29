package com.mintrocket.data.di

import androidx.room.Room
import com.mintrocket.data.database.AppDatabase
import org.koin.dsl.module

val dbModule = module {

    single {
        Room.databaseBuilder(
            get(),
            AppDatabase::class.java, "mintrocket_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        get<AppDatabase>().getUserDao()
    }
}