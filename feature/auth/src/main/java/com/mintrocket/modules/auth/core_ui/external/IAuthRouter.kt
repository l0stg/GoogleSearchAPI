package com.mintrocket.modules.auth.core_ui.external

import androidx.fragment.app.Fragment

interface IAuthRouter {

    fun popScreen()

    fun openFragment(fabric: () -> Fragment)

    fun openFragmentAsHome(fabric: () -> Fragment)

    fun openOnSuccess()

    fun openLegalDoc(legalId: String)
}