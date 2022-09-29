package com.mintrocket.navigation.commands

import com.mintrocket.datacore.utils.TextContainer
import ru.terrakok.cicerone.commands.Command

internal class ShowSnackbarCommand(
    val messageText: TextContainer,
    val actionText: TextContainer?,
    val action: (() -> Unit)?,
    val onDismissAction: (() -> Unit)?
) : Command