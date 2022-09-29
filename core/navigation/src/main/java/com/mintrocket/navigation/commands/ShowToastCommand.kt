package com.mintrocket.navigation.commands

import com.mintrocket.datacore.utils.TextContainer
import ru.terrakok.cicerone.commands.Command

internal class ShowToastCommand(
    val message: TextContainer
) : Command