package com.mintrocket.data.model.mappers

import com.mintrocket.data.model.db.UserDb
import com.mintrocket.data.model.domain.User
import com.mintrocket.data.model.network.auth.AuthUser

fun AuthUser.toDomain() =
    User(id, name, surname, email, hasPremium == 1, notificationsEnabled ?: false)

fun AuthUser.toDb() =
    UserDb(id, name, surname, email, hasPremium == 1, notificationsEnabled ?: false)