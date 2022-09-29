package com.mintrocket.data.model.mappers

import com.mintrocket.data.model.db.UserDb
import com.mintrocket.data.model.domain.User

fun User.toDb() = UserDb(
    id, name, surname, email, hasPremium, notificationsEnabled
)