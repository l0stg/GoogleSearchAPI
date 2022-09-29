package com.mintrocket.navigation.navigator

import android.content.DialogInterface
import com.mintrocket.datacore.errorhandling.ApiException
import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.navigation.AppRouter
import com.mintrocket.navigation.R
import com.mintrocket.navigation.commands.*
import com.mintrocket.navigation.navigator.holder.AppNavigatorHolder
import com.mintrocket.navigation.screens.BaseAppScreen
import com.mintrocket.navigation.screens.CustomAnimation
import com.mintrocket.navigation.screens.DialogFragmentScreen
import com.mintrocket.navigation.screens.FragmentWithResultScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.terrakok.cicerone.Cicerone

open class BaseNavigator {
    private val cicerone by lazy { Cicerone.create(AppRouter()) }

    var defaultFragmentAnimation: CustomAnimation? = null

    fun setNavigator(navigator: AppNavigatorHolder) {
        cicerone.navigatorHolder.setNavigator(navigator)

    }

    fun removeNavigator() {
        cicerone.navigatorHolder.removeNavigator()
    }

    open fun navigateTo(screen: BaseAppScreen) {
        if (screen.customAnimation == null) {
            defaultFragmentAnimation?.let { screen.withCustomAnimation(it) }
        }

        cicerone.router.navigateTo(screen)
    }

    open fun replaceTo(screen: BaseAppScreen) {
        if (screen.customAnimation == null) {
            defaultFragmentAnimation?.let { screen.withCustomAnimation(it) }
        }

        cicerone.router.replaceScreen(screen)
    }

    open fun backTo(screen: BaseAppScreen) {
        cicerone.router.backTo(screen)
    }

    open fun popScreen() {
        cicerone.router.exit()
    }

    open fun finish() {
        cicerone.router.finishChain()
    }

    open fun setRootScreen(screen: BaseAppScreen) {
        if (screen.customAnimation == null) {
            defaultFragmentAnimation?.let { screen.withCustomAnimation(it) }
        }
        cicerone.router.newRootScreen(screen)
    }

    open fun setRootChain(screens: Array<BaseAppScreen>) {
        cicerone.router.newRootChain(*screens)
    }

    open fun showDialogFragment(screen: DialogFragmentScreen) {
        cicerone.router.executeCommand(
            ShowDialogFragmentCommand(screen)
        )
    }

    open fun dismissDialogFragment(tag: String) {
        cicerone.router.executeCommand(
            DismissDialogFragmentCommand(tag)
        )
    }

    // Experimental function
    open fun handleErrorOnMain(error: Throwable, handleAuth: Boolean = true) {
        GlobalScope.launch(Dispatchers.Main) {
            handleError(error, handleAuth)
        }
    }

    open fun handleError(error: Throwable, handleAuth: Boolean = true) {
        cicerone.router.executeCommand(
            HandleErrorCommand(error, handleAuth)
        )
    }

    open fun shareText(title: TextContainer, text: TextContainer) {
        cicerone.router.executeCommand(
            ShareTextCommand(text, title)
        )
    }

    open fun startScreenForResult(screen: FragmentWithResultScreen) {
        if (screen.customAnimation == null) {
            defaultFragmentAnimation?.let { screen.withCustomAnimation(it) }
        }

        cicerone.router.navigateTo(screen)
    }

    open fun showErrorWithRetryDialog(exception: ApiException, retryAction: () -> Unit) {
        showDialog(
            title = exception.titleContainer,
            message = exception.messageContainer,
            positiveButtonText = TextContainer.ResContainer(R.string.retry),
            positiveButtonAction = { di, _ ->
                retryAction()
                di.dismiss()
            }
        )
    }

    open fun showDialog(
        title: TextContainer? = null,
        message: TextContainer? = null,
        positiveButtonText: TextContainer? = null,
        neutralButtonText: TextContainer? = null,
        negativeButtonText: TextContainer? = null,
        positiveButtonAction: ((DialogInterface, Int) -> Unit)? = null,
        neutralButtonAction: ((DialogInterface, Int) -> Unit)? = null,
        negativeButtonAction: ((DialogInterface, Int) -> Unit)? = null,
        onDismissAction: ((DialogInterface) -> Unit)? = null,
        onCancelAction: ((DialogInterface) -> Unit)? = null,
        cancelable: Boolean = true
    ) {
        cicerone.router.executeCommand(
            AlertDialogCommand(
                title, message, positiveButtonText,
                neutralButtonText, negativeButtonText,
                positiveButtonAction, neutralButtonAction, negativeButtonAction, onDismissAction,
                onCancelAction, cancelable
            )
        )
    }

    open fun showToast(text: TextContainer) {
        cicerone.router.executeCommand(ShowToastCommand(text))
    }

    open fun showSnackBar(
        text: TextContainer,
        action: (() -> Unit)? = null,
        actionText: TextContainer? = null,
        onDismissAction: (() -> Unit)? = null
    ) {
        cicerone.router.executeCommand(
            ShowSnackbarCommand(text, actionText, action, onDismissAction)
        )
    }

    open fun viewContent(url: String) {
        cicerone.router.executeCommand(ViewContentCommand(url))
    }
}