package com.mintrocket.navigation.navigator.holder

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.FrameLayout
import androidx.core.app.ShareCompat
import androidx.fragment.app.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.navigation.NavigatorContainer
import com.mintrocket.navigation.R
import com.mintrocket.navigation.commands.*
import com.mintrocket.navigation.screens.CustomAnimation
import com.mintrocket.navigation.screens.CustomSupportScreen
import com.mintrocket.uicore.getExtra
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.BackTo
import ru.terrakok.cicerone.commands.Command
import ru.terrakok.cicerone.commands.Replace
import timber.log.Timber

open class AppNavigatorHolder : SupportAppNavigator {

    private val navigatorContainer: NavigatorContainer
    private val context: Context

    constructor(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        navContainer: NavigatorContainer,
        containerId: Int
    )
            : super(activity, fragmentManager, containerId) {
        navigatorContainer = navContainer
        context = activity
    }

    constructor(
        activity: FragmentActivity,
        navContainer: NavigatorContainer,
        containerId: Int
    ) : super(activity, containerId) {
        navigatorContainer = navContainer
        context = activity
    }

    override fun setupFragmentTransaction(
        command: Command?,
        currentFragment: Fragment?,
        nextFragment: Fragment?,
        fragmentTransaction: FragmentTransaction?
    ) {
        nextFragment?.getExtra<CustomAnimation>(
            CustomSupportScreen.ARG_ANIMATION
        )
            ?.let { anim ->
                fragmentTransaction?.setCustomAnimations(
                    anim.enter, anim.exit, anim.popEnter, anim.popExit
                )
            }

        val sourceSharedElement =
            nextFragment?.getExtra<String>(CustomSupportScreen.ARG_SHARED_TRANSITION_NAME_SOURCE)
        val destSharedElement =
            nextFragment?.getExtra<String>(CustomSupportScreen.ARG_SHARED_TRANSITION_NAME_DEST)

        runCatching {
            if (sourceSharedElement != null && destSharedElement != null) {
                val view = FrameLayout(currentFragment!!.requireContext())
                view.transitionName = sourceSharedElement
                fragmentTransaction?.addSharedElement(view, destSharedElement)
            }
        }.onFailure { Timber.e(it) }
    }

    override fun applyCommand(command: Command?) {
        when (command) {
            is AlertDialogCommand -> runAlertDialog(command)
            is ShowDialogFragmentCommand -> runDialogFragment(command)
            is DismissDialogFragmentCommand -> dismissDialogFragment(command)
            is ShowToastCommand -> showToast(command)
            is ShowSnackbarCommand -> showSnackbar(command)
            is HandleErrorCommand -> handleError(command)
            is ViewContentCommand -> viewContent(command)
            is ShareTextCommand -> shareText(command)
            else -> super.applyCommand(command)
        }
    }

    private fun viewContent(command: ViewContentCommand) {
        try {
            val uri = Uri.parse(command.url)
            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
        } catch (ex: Exception) {
            applyCommand(ShowToastCommand(TextContainer.ResContainer(R.string.invalid_url)))
            Timber.e(ex)
        }
    }

    private fun handleError(command: HandleErrorCommand) {
        navigatorContainer.handleError(command.throwable, command.handleAuth)?.let {
            applyCommand(BackTo(null))
            applyCommand(Replace(it))
        }
    }

    private fun dismissDialogFragment(command: DismissDialogFragmentCommand) {
        navigatorContainer.dismissDialogFragment(command.tag)
    }

    private fun runDialogFragment(command: ShowDialogFragmentCommand) {
        navigatorContainer.showDialogFragment(
            command.screen.getFragment() as DialogFragment, command.screen.tag
        )
    }

    private fun showToast(command: ShowToastCommand) {
        navigatorContainer.showToast(command.message)
    }

    private fun showSnackbar(command: ShowSnackbarCommand) {
        navigatorContainer.showSnackBar(
            command.messageText, command.actionText, command.action, command.onDismissAction
        )
    }

    private fun shareText(command: ShareTextCommand) {
        (context as? Activity)?.let {
            ShareCompat.IntentBuilder.from(it)
                .setType("text/plain") // or "message/rfc822"
                .setChooserTitle(command.title.getTextValue(it))
                .setText(command.text.getTextValue(it))
                .startChooser()
        }
    }

    private fun runAlertDialog(command: AlertDialogCommand) {
        with(command) {
            val builder = MaterialAlertDialogBuilder(context)

            title?.let {
                builder.setTitle(it.getTextValue(context))
            }

            message?.let {
                builder.setMessage(it.getTextValue(context))
            }

            if (positiveButtonAction != null) {
                builder.setPositiveButton(
                    positiveButtonText?.getTextValue(context)
                ) { dialog, which ->
                    positiveButtonAction.invoke(dialog, which)
                }
            }

            if (neutralButtonAction != null) {
                builder.setNeutralButton(
                    neutralButtonText?.getTextValue(context)
                ) { dialog, which ->
                    neutralButtonAction.invoke(dialog, which)
                }
            }

            if (negativeButtonAction != null) {
                builder.setNegativeButton(
                    negativeButtonText?.getTextValue(context)
                ) { dialog, which ->
                    negativeButtonAction.invoke(dialog, which)
                }
            }

            builder.setOnCancelListener { onCancelAction?.invoke(it) }
                .setOnDismissListener { onDismissAction?.invoke(it) }
                .setCancelable(cancelable)

            navigatorContainer.showAlertDialog(builder.create())
        }
    }
}