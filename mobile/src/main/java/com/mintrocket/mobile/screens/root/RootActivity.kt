package com.mintrocket.mobile.screens.root

import android.os.Bundle
import com.mintrocket.mobile.R
import com.mintrocket.navigation.containers.ContainerActivity
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.navigation.navigator.holder.AppNavigatorHolder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class RootActivity : ContainerActivity() {

    override val navigator by inject<ApplicationNavigator>()
    override val navigatorHolder =
        AppNavigatorHolder(
            this,
            this,
            R.id.fragmentContainer
        )

    private val viewModel by viewModel<RootViewModel>()

    override val containerId: Int = R.id.fragmentContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            viewModel.initNavigation()
        }
    }
}
