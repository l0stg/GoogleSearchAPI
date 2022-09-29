package com.mintrocket.uicore.systembar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.core.view.WindowInsetsControllerCompat

// Cyan, чтобы сразу было видно что что-то где-то не так
private const val DEFAULT_COLOR = Color.CYAN
private const val DEFAULT_LIGHT = false

fun Activity.setLightStatusBar(isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setSystemUiBool(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, isLight)
    }
}

fun Activity.setLightNavigationBar(isLight: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        setSystemUiBool(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR, isLight)
    }
}

fun Activity.setStatusBarColor(@ColorInt color: Int) {
    window.statusBarColor = color
}

fun Activity.setNavigationBarColor(@ColorInt color: Int) {
    window.navigationBarColor = color
}

// Current
fun Activity.getStatusBarColor(): Int = window.statusBarColor

fun Activity.getNavigationBarColor(): Int = window.navigationBarColor

fun Activity.getLightStatusBar(): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getSystemUiBool(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    } else {
        DEFAULT_LIGHT
    }

fun Activity.getLightNavigationBar(): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        getSystemUiBool(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    } else {
        DEFAULT_LIGHT
    }

// Theme
fun Context.getThemeStatusBarColor(): Int = getThemeColor(android.R.attr.statusBarColor)

fun Context.getThemeNavigationBarColor(): Int = getThemeColor(android.R.attr.navigationBarColor)

fun Context.getThemeLightStatusBar(): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getThemeBool(android.R.attr.windowLightStatusBar)
    } else {
        DEFAULT_LIGHT
    }

fun Context.getThemeLightNavigationBar(): Boolean =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
        getThemeBool(android.R.attr.windowLightNavigationBar)
    } else {
        DEFAULT_LIGHT
    }


// Common
private fun Context.getThemeColor(@AttrRes attr: Int): Int {
    return theme.obtainStyledAttributes(intArrayOf(attr)).use {
        it.getColor(0, DEFAULT_COLOR)
    }
}

private fun Context.getThemeBool(@AttrRes attr: Int): Boolean {
    return theme.obtainStyledAttributes(intArrayOf(attr)).use {
        it.getBoolean(0, DEFAULT_LIGHT)
    }
}

private fun Activity.getSystemUiBool(flag: Int): Boolean {
    val flags = window.decorView.systemUiVisibility
    return (flags and flag) == flag
}

private fun Activity.setSystemUiBool(flag: Int, value: Boolean) {
    var flags = window.decorView.systemUiVisibility
    flags = if (value) {
        flags or flag
    } else {
        flags and flag.inv()
    }
    window.decorView.systemUiVisibility = flags
}