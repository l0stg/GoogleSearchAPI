package com.mintrocket.security.example.di.security

import android.content.Context
import com.mintrocket.modules.security.external.SecurityLaunchRouter
import com.mintrocket.security.example.screens.PinBarrierScreen

class SecurityLaunchRouterImpl(
    private val context: Context
) : SecurityLaunchRouter {

    override fun openBarrier() {
        context.startActivity(PinBarrierScreen().getActivityIntent(context))
    }
}