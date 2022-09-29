package com.mintrocket.navigation

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.navigation.screens.BaseAppScreen


interface NavigatorContainer {
    fun showAlertDialog(dialog: AlertDialog)

    fun dismissDialogFragment(tag: String)

    fun showDialogFragment(
        dialog: DialogFragment,
        tag: String
    )

    fun showToast(text: TextContainer)

    fun showSnackBar(
        text: TextContainer,
        actionText: TextContainer?,
        action: (() -> Unit)?,
        dismissAction: (() -> Unit)?
    )

    fun handleError(
        throwable: Throwable,
        requiredAuthRedirect: Boolean
    ): BaseAppScreen?
}