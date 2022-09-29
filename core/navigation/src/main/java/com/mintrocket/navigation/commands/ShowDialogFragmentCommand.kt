package com.mintrocket.navigation.commands

import com.mintrocket.navigation.screens.DialogFragmentScreen
import ru.terrakok.cicerone.commands.Command

internal class ShowDialogFragmentCommand(
    val screen: DialogFragmentScreen
) : Command