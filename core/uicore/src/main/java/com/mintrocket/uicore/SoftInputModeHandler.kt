package com.mintrocket.uicore

import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

private typealias WRT = WindowManager.LayoutParams

class SoftInputModeHandler(
    private val type: ResizeType,
    private val restoreDefaultOnPause: Boolean = true
) : DefaultLifecycleObserver {

    private var fragment: Fragment? = null
    private var oldResizeType: ResizeType? = null

    fun attachToFragment(fragment: Fragment) {
        this.fragment = fragment
        fragment.lifecycle.addObserver(this)
        oldResizeType = fragment.activity?.window
            ?.attributes?.softInputMode?.toResizeType()
    }

    fun detach() {
        fragment?.lifecycle?.removeObserver(this)
        fragment = null
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        setDesiredResizeType()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        setOldResizeType()
    }

    private fun setDesiredResizeType() {
        changeResizeType(type)
    }

    private fun setOldResizeType() {
        oldResizeType?.takeIf { restoreDefaultOnPause }
            ?.let { changeResizeType(it) }
    }

    private fun changeResizeType(resizeType: ResizeType) {
        fragment?.activity
            ?.window
            ?.setSoftInputMode(
                resizeType.toWRTMode()
            )
    }

    private fun ResizeType.toWRTMode() = when (this) {
        ResizeType.ADJUST_PAN -> WRT.SOFT_INPUT_ADJUST_PAN
        ResizeType.RESIZE -> WRT.SOFT_INPUT_ADJUST_RESIZE
        ResizeType.NOTHING -> WRT.SOFT_INPUT_ADJUST_NOTHING
    }

    private fun Int.toResizeType() = when (this) {
        WRT.SOFT_INPUT_ADJUST_PAN -> ResizeType.ADJUST_PAN
        WRT.SOFT_INPUT_ADJUST_RESIZE -> ResizeType.RESIZE
        else -> ResizeType.NOTHING
    }

    enum class ResizeType {
        ADJUST_PAN,
        RESIZE,
        NOTHING
    }
}