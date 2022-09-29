package com.mintrocket.mobile.di

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.mintrocket.navigation.ScopeNameWrapper
import com.mintrocket.navigation.navigator.holder.ScopedNavigatorHolder
import com.mintrocket.uicore.getExtra
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier

inline fun <reified T : ViewModel> Fragment.scopeViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> = lazy {
    getViewModel(qualifier) {
        val scopeName = requireNotNull(findNearestScopeName()) {
            ScopedNavigatorHolder.ARG_SCOPE_NAME
        }
        parameters?.invoke()?.also { paramsHolder ->
            paramsHolder.insert(0, scopeName)
        } ?: parametersOf(scopeName)
    }
}

inline fun <reified T : ViewModel> Fragment.optionScopeViewModel(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null,
): Lazy<T> = lazy {
    getViewModel(qualifier) {
        val scopeNameWrapper = ScopeNameWrapper(findNearestScopeName())
        parameters?.invoke()?.also { paramsHolder ->
            paramsHolder.insert(0, scopeNameWrapper)
        } ?: parametersOf(scopeNameWrapper)
    }
}

fun Fragment.findNearestScopeName(): String? {
    val allScopes = mutableListOf<String?>()
    var currentFragment: Fragment? = this
    while (currentFragment != null) {
        allScopes.add(currentFragment.getExtra(ScopedNavigatorHolder.ARG_SCOPE_NAME))
        currentFragment = currentFragment.parentFragment
    }
    return allScopes.filterNotNull().firstOrNull()
}