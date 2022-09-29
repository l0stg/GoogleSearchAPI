package com.mintrocket.showcase

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.navigation.containers.ContainerActivity
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.navigation.navigator.holder.AppNavigatorHolder
import com.mintrocket.navigation.screens.BaseAppScreen
import com.mintrocket.showcase.databinding.ActivityMainBinding
import org.koin.android.ext.android.inject

class MainActivity : ContainerActivity() {

    override val navigator by inject<ApplicationNavigator>()
    override val containerId: Int = R.id.fragmentContainer
    override val navigatorHolder = AppNavigatorHolder(
        this,
        this,
        R.id.fragmentContainer
    )

    private val binding by viewBinding<ActivityMainBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        binding.content.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION

        if (savedInstanceState == null) {
            navigator.setRootScreen(FeaturesScreen())
        }
    }

    override fun getSnackBarContainer() = binding.content

    override fun handleError(throwable: Throwable, requiredAuthRedirect: Boolean): BaseAppScreen? {
        //ignore errors
        return null
    }
}