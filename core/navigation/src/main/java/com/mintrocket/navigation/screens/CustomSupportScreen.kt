package com.mintrocket.navigation.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.Fragment
import ru.terrakok.cicerone.android.support.SupportAppScreen

abstract class CustomSupportScreen : SupportAppScreen() {
    companion object {
        const val ARG_ANIMATION = "CustomSupportScreen:extra_animation"
        const val ARG_SHARED_TRANSITION_NAME_SOURCE =
            "CustomSupportScreen:extra_transition_name_source"
        const val ARG_SHARED_TRANSITION_NAME_DEST = "CustomSupportScreen:extra_transition_name_dest"

        val NO_ANIMATION = CustomAnimation(0, 0, 0, 0)
    }

    var customAnimation: CustomAnimation? = null
        private set
    private var sourceSharedName: String? = null
    private var destinationSharedName: String? = null

    abstract fun createFragment(): Fragment?

    abstract fun createIntent(context: Context): Intent?

    override fun getFragment() = createFragment()?.apply { proceedExtras(this) }

    override fun getActivityIntent(context: Context) = createIntent(context)

    fun withCustomAnimations(
        @AnimatorRes @AnimRes enter: Int,
        @AnimatorRes @AnimRes exit: Int,
        @AnimatorRes @AnimRes popEnter: Int,
        @AnimatorRes @AnimRes popExit: Int
    ) {
        customAnimation = CustomAnimation(
            enter,
            exit,
            popEnter,
            popExit
        )
    }

    fun withCustomAnimation(
        animation: CustomAnimation?
    ) {
        customAnimation = animation
    }

    fun withSharedElement(
        sourceName: String,
        destinationName: String
    ) {
        sourceSharedName = sourceName
        destinationSharedName = destinationName
    }

    private fun proceedExtras(fragment: Fragment) {
        if (!bundlesRequired()) return
        val args = fragment.arguments ?: Bundle().apply { fragment.arguments = this }
        customAnimation?.takeIf { it != NO_ANIMATION }
            ?.let { args.putParcelable(ARG_ANIMATION, it) }
        sourceSharedName?.let { args.putString(ARG_SHARED_TRANSITION_NAME_SOURCE, it) }
        destinationSharedName?.let { args.putString(ARG_SHARED_TRANSITION_NAME_DEST, it) }
    }

    private fun bundlesRequired(): Boolean =
        customAnimation != null ||
                sourceSharedName != null ||
                destinationSharedName != null
}

data class CustomAnimation(
    @AnimatorRes @AnimRes val enter: Int,
    @AnimatorRes @AnimRes val exit: Int,
    @AnimatorRes @AnimRes val popEnter: Int,
    @AnimatorRes @AnimRes val popExit: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(enter)
        parcel.writeInt(exit)
        parcel.writeInt(popEnter)
        parcel.writeInt(popExit)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CustomAnimation> {
        override fun createFromParcel(parcel: Parcel): CustomAnimation {
            return CustomAnimation(parcel)
        }

        override fun newArray(size: Int): Array<CustomAnimation?> {
            return arrayOfNulls(size)
        }
    }
}