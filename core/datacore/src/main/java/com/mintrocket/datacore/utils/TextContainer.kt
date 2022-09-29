package com.mintrocket.datacore.utils

import android.content.Context
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class TextContainer : Parcelable {

    fun getTextValue(context: Context): String {
        return when (this) {
            is StringContainer -> text
            is ResContainer -> context.getString(text)
            is StringFormatContainer -> context.getString(text, *args.toTypedArray())
        }
    }

    @Parcelize
    data class StringContainer(val text: String) : TextContainer(), Parcelable

    @Parcelize
    data class ResContainer(val text: Int) : TextContainer(), Parcelable

    @Parcelize
    data class StringFormatContainer(
        val text: Int,
        val args: List<String>
    ) : TextContainer(), Parcelable
}

fun String.toContainer() = TextContainer.StringContainer(this)