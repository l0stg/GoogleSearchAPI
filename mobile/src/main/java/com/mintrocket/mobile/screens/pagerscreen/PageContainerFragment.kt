package com.mintrocket.mobile.screens.pagerscreen

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.mobile.R
import com.mintrocket.navigation.BackButtonListener
import com.mintrocket.navigation.NavigatorContainer
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.navigation.navigator.holder.ScopedNavigatorHolder
import com.mintrocket.navigation.screens.BaseAppScreen
import com.mintrocket.uicore.extraNotNull
import org.koin.android.ext.android.get

class PageContainerFragment : Fragment(R.layout.fragment_page_container),
    NavigatorContainer, BackButtonListener {

    companion object {
        private const val EXTRA_SCOPE_NAME = "extra_scope_name"

        fun newInstance(scopeName: String) = PageContainerFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_SCOPE_NAME, scopeName)
            }
        }
    }

    private val scopeName by extraNotNull<String>(EXTRA_SCOPE_NAME)
    private val navigator by lazy {
        get<ApplicationNavigator>().getScopedNavigator(scopeName)
    }
    private val navigatorHolder by lazy {
        ScopedNavigatorHolder(
            requireActivity(),
            childFragmentManager,
            this,
            R.id.fragmentContainerPage,
            scopeName
        )
    }

    override fun onResume() {
        super.onResume()
        navigator.setNavigator(navigatorHolder)
    }

    override fun onPause() {
        super.onPause()
        navigator.removeNavigator()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (childFragmentManager.fragments.isEmpty()) {
            navigator.setRootScreen(PagerHelper.getScreenForScopeName(scopeName))
        }
    }

    override fun showAlertDialog(dialog: AlertDialog) {
        // Do nothing. Should called in activity navigation container.
    }

    override fun dismissDialogFragment(tag: String) {
        // Do nothing. Should called in activity navigation container.
    }

    override fun showDialogFragment(dialog: DialogFragment, tag: String) {
        // Do nothing. Should called in activity navigation container.
    }

    override fun showToast(text: TextContainer) {
        // Do nothing. Should called in activity navigation container.
    }

    override fun showSnackBar(
        text: TextContainer,
        actionText: TextContainer?,
        action: (() -> Unit)?,
        dismissAction: (() -> Unit)?
    ) {
        // Do nothing. Should called in activity navigation container.
    }

    override fun handleError(throwable: Throwable, requiredAuthRedirect: Boolean): BaseAppScreen? {
        // Do nothing. Should called in activity navigation container.
        return null
    }

    override fun onBackPressed(): Boolean {
        val fragment = childFragmentManager.findFragmentById(R.id.fragmentContainerPage)
        val fragmentBackHandled = (fragment as? BackButtonListener)?.onBackPressed() ?: false
        val canPopScreen = childFragmentManager.backStackEntryCount >= 1
        return when {
            fragmentBackHandled -> true
            canPopScreen -> {
                navigator.popScreen()
                true
            }
            else -> false
        }
    }
}