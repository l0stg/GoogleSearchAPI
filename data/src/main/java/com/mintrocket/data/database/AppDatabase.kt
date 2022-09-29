package com.mintrocket.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mintrocket.data.database.converters.DateConverter
import com.mintrocket.data.model.db.UserDb

@Database(
    entities = [UserDb::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}