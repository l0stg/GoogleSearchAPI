package com.mintrocket.navigation.commands

import ru.terrakok.cicerone.commands.Command

internal data class HandleErrorCommand(
    val throwable: Throwable,
    val handleAuth: Boolean
) : Command