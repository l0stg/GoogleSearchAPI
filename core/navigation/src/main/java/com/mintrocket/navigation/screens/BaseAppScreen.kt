package com.mintrocket.navigation.screens

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

sealed class BaseAppScreen : CustomSupportScreen() {
    fun withFadeInOutAnimation(): BaseAppScreen {
        withCustomAnimations(
            android.R.animator.fade_in, android.R.animator.fade_out,
            android.R.animator.fade_in, android.R.animator.fade_out
        )
        return this
    }

    fun withSlideSideAnimation(): BaseAppScreen {
        withCustomAnimations(
            android.R.anim.slide_in_left, android.R.anim.slide_out_right,
            android.R.anim.slide_in_left, android.R.anim.slide_out_right
        )
        return this
    }

    inline fun <reified T : BaseAppScreen> withoutAnimation(): T {
        withCustomAnimation(NO_ANIMATION)
        return this as T
    }
}

abstract class FragmentScreen : BaseAppScreen() {
    override fun createIntent(context: Context): Intent? = null
}

abstract class ActivityScreen : BaseAppScreen() {
    override fun createFragment(): Fragment? = null
}

abstract class FragmentWithResultScreen(val tag: String) : FragmentScreen()