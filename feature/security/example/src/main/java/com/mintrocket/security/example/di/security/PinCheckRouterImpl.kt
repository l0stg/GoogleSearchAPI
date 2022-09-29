package com.mintrocket.security.example.di.security

import androidx.lifecycle.MutableLiveData
import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.datacore.utils.toContainer
import com.mintrocket.modules.security.external.PinCheckRouter
import com.mintrocket.modules.security.screens.PinCheckResult
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.security.example.R
import com.mintrocket.datacore.Event

class PinCheckRouterImpl(
    private val navigator: ApplicationNavigator,
    private val resultsFlow: MutableLiveData<Event<PinCheckResult>>
) : PinCheckRouter {

    override fun exit(result: PinCheckResult) {
        resultsFlow.value = Event(result)
        navigator.popScreen()
    }

    override fun showWrongCode() {
        navigator.showToast(TextContainer.ResContainer(R.string.security_check_wrong_code))
    }

    override fun showMsg(msg: String) {
        navigator.showToast(msg.toContainer())
    }
}