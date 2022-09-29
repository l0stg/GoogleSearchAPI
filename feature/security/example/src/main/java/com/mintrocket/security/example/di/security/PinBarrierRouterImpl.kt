package com.mintrocket.security.example.di.security

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.mintrocket.datacore.utils.TextContainer
import com.mintrocket.datacore.utils.toContainer
import com.mintrocket.modules.security.external.PinBarrierRouter
import com.mintrocket.modules.security.screens.CheckResultReason
import com.mintrocket.modules.security.screens.PinCheckResult
import com.mintrocket.navigation.navigator.ApplicationNavigator
import com.mintrocket.security.example.R
import com.mintrocket.security.example.screens.MainScreen
import com.mintrocket.datacore.Event

class PinBarrierRouterImpl(
    private val activity: AppCompatActivity,
    private val resultsFlow: MutableLiveData<Event<PinCheckResult>>
) : PinBarrierRouter {

    override fun exit(result: PinCheckResult) {
        resultsFlow.value = Event(result)
        when (result) {
            PinCheckResult.Success -> {
                activity.finish()
            }
            is PinCheckResult.Cancel -> {
                if (result.reason == CheckResultReason.LOGOUT) {
                    activity.startActivity(MainScreen(true).getActivityIntent(activity))
                } else {
                    activity.finishAffinity()
                }
            }
        }
    }

    override fun showWrongCode() {
        Toast.makeText(activity, R.string.security_barrier_wrong_code, Toast.LENGTH_SHORT).show()
    }

    override fun showMsg(msg: String) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show()
    }
}