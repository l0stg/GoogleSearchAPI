package com.mintrocket.data.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mintrocket.data.model.domain.User

@Entity(tableName = "profile_table")
data class UserDb(
    @PrimaryKey
    val id: Long,
    val name: String,
    val surname: String,
    val email: String,
    val hasPremium: Boolean,
    val notificationsEnabled: Boolean
)

fun UserDb.toDomain() = User(
    id, name, surname, email, hasPremium, notificationsEnabled
)