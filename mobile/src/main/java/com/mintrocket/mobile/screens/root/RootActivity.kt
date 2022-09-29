package com.mintrocket.mobile.screens.root

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.observe
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mintrocket.datacore.errorhandling.AuthorizationRequiredException
import com.mintrocket.datacore.errorhandling.IErrorHandler
import com.mintrocket.mobile.R
import com.mintrocket.mobile.common.InAppUpdateHelper
import com.mintrocket.mobile.databinding.ActivityMainBinding
import com.mintrocket.navigation.containers.ContainerActivity
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.navigation.navigator.holder.AppNavigatorHolder
import com.mintrocket.navigation.screens.BaseAppScreen
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

abstract class RootActivity : ContainerActivity() {

    override val navigator by inject<ApplicationNavigator>()
    override val navigatorHolder =
        AppNavigatorHolder(
            this,
            this,
            R.id.fragmentContainer
        )

    private val viewModel by viewModel<RootViewModel>()
    private val errorHandler by inject<IErrorHandler>()

    private val binding by viewBinding<ActivityMainBinding>()

    private val inAppUpdateHelper = InAppUpdateHelper(this)

    override val containerId: Int = R.id.fragmentContainer

    override fun getSnackBarContainer() = binding.content

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            viewModel.initNavigation()
        }

        viewModel.progressLD.observe(this) { binding.progressMainContainer.isVisible = it }
    }

    /**
     * Handle error and show some message if required
     * if error requires redirect return a screen
     */
    override fun handleError(throwable: Throwable, requiredAuthRedirect: Boolean): BaseAppScreen? {
        val ex = errorHandler.parseThrowable(throwable)
        Timber.e(throwable)
        return when {
            ex is AuthorizationRequiredException && requiredAuthRedirect -> {
                showToast(ex.messageContainer)
                viewModel.logout()
                //todo handle with auth feature
                return null
            }
            else -> {
                showToast(ex.messageContainer)
                null
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == InAppUpdateHelper.APP_UPDATE_REQUEST_CODE) {
            inAppUpdateHelper.onActivityResult(resultCode)
        }
    }
}
