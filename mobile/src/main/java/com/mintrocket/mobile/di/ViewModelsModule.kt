package com.mintrocket.mobile.di

import com.mintrocket.mobile.screens.contentlist.ContentListViewModel
import com.mintrocket.mobile.screens.root.RootViewModel
import com.mintrocket.navigation.navigator.ApplicationNavigator
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.scope.Scope
import org.koin.dsl.module

val viewModelsModule = module {
    viewModel { RootViewModel(get()) }

    viewModel {
        ContentListViewModel(get())
    }
}

private fun Scope.getNestedNavigator(name: String) =
    get<ApplicationNavigator>().getScopedNavigator(name)