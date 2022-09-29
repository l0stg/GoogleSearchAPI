package com.mintrocket.navigation.commands

import com.mintrocket.datacore.utils.TextContainer
import ru.terrakok.cicerone.commands.Command

internal data class ShareTextCommand(
    val text: TextContainer,
    val title: TextContainer
) : Command