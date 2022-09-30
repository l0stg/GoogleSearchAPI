package com.mintrocket.mobile.screens.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mintrocket.mobile.navigation.ContentListScreen
import com.mintrocket.navigation.navigator.ApplicationNavigator
import kotlinx.coroutines.launch

class RootViewModel(
    private val navigator: ApplicationNavigator,
) : ViewModel() {

    fun initNavigation() {
        viewModelScope.launch {
            navigator.setRootScreen(ContentListScreen())
        }
    }
}