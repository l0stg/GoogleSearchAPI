package com.mintrocket.navigation.containers

import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.navigation.navigator.holder.AppNavigatorHolder
import com.mintrocket.navigation.BackButtonListener
import com.mintrocket.navigation.navigator.BaseNavigator
import com.mintrocket.navigation.NavigatorContainer
import com.mintrocket.uicore.dismissDialogFragmentIfShown
import com.mintrocket.uicore.toast

abstract class ContainerActivity : AppCompatActivity(), NavigatorContainer {

    abstract val navigator: BaseNavigator

    abstract val containerId: Int

    abstract val navigatorHolder: AppNavigatorHolder

    private var shownDialog: AlertDialog? = null

    override fun onResume() {
        super.onResume()
        navigator.setNavigator(navigatorHolder)
    }

    override fun onPause() {
        super.onPause()
        navigator.removeNavigator()
    }

    override fun onStop() {
        super.onStop()
        shownDialog?.dismiss()
        shownDialog = null
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(containerId)
        val backHandled = (fragment as? BackButtonListener)?.onBackPressed() ?: false
        if (!backHandled) super.onBackPressed()
    }

    override fun showAlertDialog(dialog: AlertDialog) {
        if (shownDialog?.isShowing == true) {
            shownDialog?.dismiss()
        }

        dialog.show()
        shownDialog = dialog
    }

    override fun dismissDialogFragment(tag: String) {
        dismissDialogFragmentIfShown(tag)
    }

    override fun showDialogFragment(dialog: DialogFragment, tag: String) {
        dismissDialogFragment(tag)
        dialog.show(supportFragmentManager, tag)
    }

    override fun showToast(text: TextContainer) {
        toast(text.getTextValue(this))
    }

}