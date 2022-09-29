package com.mintrocket.navigation.commands

import android.content.DialogInterface
import com.mintrocket.datacore.utils.TextContainer
import ru.terrakok.cicerone.commands.Command

internal class AlertDialogCommand(
    val title: TextContainer? = null,
    val message: TextContainer? = null,
    val positiveButtonText: TextContainer? = null,
    val neutralButtonText: TextContainer? = null,
    val negativeButtonText: TextContainer? = null,
    val positiveButtonAction: ((DialogInterface, Int) -> Unit)? = null,
    val neutralButtonAction: ((DialogInterface, Int) -> Unit)? = null,
    val negativeButtonAction: ((DialogInterface, Int) -> Unit)? = null,
    val onDismissAction: ((DialogInterface) -> Unit)? = null,
    val onCancelAction: ((DialogInterface) -> Unit)? = null,
    val cancelable: Boolean = true
) : Command