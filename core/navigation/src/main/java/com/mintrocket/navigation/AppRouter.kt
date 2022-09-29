package com.mintrocket.navigation

import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.commands.Command

internal class AppRouter : Router() {
    fun executeCommand(command: Command) {
        executeCommands(command)
    }
}