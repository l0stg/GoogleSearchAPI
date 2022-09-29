package com.mintrocket.uicore

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Parcel
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

fun DialogFragment.show(fragment: Fragment, tag: String, requestCode: Int = 0) {
    setTargetFragment(fragment, requestCode)
    show(fragment.requireActivity().supportFragmentManager, tag)
}

fun Fragment.dismissDialogFragmentIfShown(tag: String) {
    (activity?.supportFragmentManager
        ?.findFragmentByTag(tag) as? DialogFragment)
        ?.dismissAllowingStateLoss()
}

fun AppCompatActivity.dismissDialogFragmentIfShown(tag: String) {
    (supportFragmentManager
        .findFragmentByTag(tag) as? DialogFragment)
        ?.dismissAllowingStateLoss()
}

inline fun <reified T : Any> Activity.getExtraNotNull(key: String, default: T? = null): T {
    val value = intent?.extras?.get(key)
    return requireNotNull(if (value is T) value else default) { key }
}

inline fun <reified T : Any> Activity.getExtra(key: String, default: T? = null): T? {
    val value = intent?.extras?.get(key)
    return if (value is T) value else default
}

inline fun <reified T : Any> Activity.extra(key: String, default: T? = null) = lazy {
    getExtra(key, default)
}

inline fun <reified T : Any> Activity.extraNotNull(key: String, default: T? = null) = lazy {
    getExtraNotNull(key, default)
}

inline fun <reified T : Any> Fragment.getExtra(key: String, default: T? = null): T? {
    val value = arguments?.get(key)
    return if (value is T) value else default
}

inline fun <reified T : Any> Fragment.getExtraNotNull(key: String, default: T? = null): T {
    val value = arguments?.get(key)
    return requireNotNull(if (value is T) value else default) { key }
}

fun Fragment.withArgs(vararg pairs: Pair<String, Any?>): Fragment {
    val newArgs = bundleOf(*pairs)
    arguments?.let {
        it.putAll(newArgs)
    } ?: run {
        arguments = newArgs
    }
    return this
}

fun DialogFragment.withArgs(vararg pairs: Pair<String, Any?>): DialogFragment {
    return (this as Fragment).withArgs(*pairs) as DialogFragment
}

inline fun <reified T : Any> Fragment.extra(key: String, default: T? = null) = lazy {
    getExtra(key, default)
}

inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    getExtraNotNull(key, default)
}

fun <T : Fragment> T.attachBackPressed(
    enabled: Boolean = true,
    block: OnBackPressedCallback.() -> Unit
): OnBackPressedCallback {
    val callback: OnBackPressedCallback = object : OnBackPressedCallback(enabled) {
        override fun handleOnBackPressed() {
            block.invoke(this)
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    return callback
}

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes message: Int) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

val Int.dpToPx: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Int.pxToDp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Float.dpToPx: Float get() = (this * Resources.getSystem().displayMetrics.density)

val Float.toScreenWidthFraction: Int get() = (this * Resources.getSystem().displayMetrics.widthPixels).toInt()

fun Bundle.getBundleSizeInBytes(): Int {
    val parcel = Parcel.obtain()
    parcel.writeValue(this)

    val size = parcel.dataSize()
    parcel.recycle()

    return size
}

fun Intent.withNewTaskFlag(): Intent {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    return this
}

fun Context.copyToClipboard(label: String, text: String) {
    val clipboard = ContextCompat.getSystemService(this, ClipboardManager::class.java)
    val clip = ClipData.newPlainText(label, text)
    clipboard?.setPrimaryClip(clip)
}