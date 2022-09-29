package com.mintrocket.uicore

import android.content.Context
import android.content.res.TypedArray
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.res.getBooleanOrThrow
import androidx.core.content.res.getColorOrThrow
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.content.res.use

@IdRes
fun Context.getResourceIdByAttr(@AttrRes attr: Int): Int = useTypeArrayByAttr(attr) {
    it.getResourceIdOrThrow(0)
}

@ColorRes
fun Context.getColorResByAttr(@AttrRes attr: Int): Int = useTypeArrayByAttr(attr) {
    it.getResourceIdOrThrow(0)
}

@ColorInt
fun Context.getColorByAttr(@AttrRes attr: Int): Int = useTypeArrayByAttr(attr) {
    it.getColorOrThrow(0)
}

fun Context.getThemeBool(@AttrRes attr: Int): Boolean = useTypeArrayByAttr(attr) {
    it.getBooleanOrThrow(0)
}

fun <T> Context.useTypeArrayByAttr(@AttrRes attr: Int, block: (TypedArray) -> T): T =
    theme.obtainStyledAttributes(intArrayOf(attr)).use {
        block.invoke(it)
    }