package com.mintrocket.modules.auth.core_ui.extensions

import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.mintrocket.modules.auth.core_ui.AuthFeatureConfig
import com.mintrocket.uicore.addClickableSpanForWord

internal fun Fragment.setupLicenseText(
    textView: TextView,
    config: AuthFeatureConfig,
    onLegalClick: (String) -> Unit
) {
    textView.isVisible = config.legalTextRes != null
    val legalTextRes = config.legalTextRes ?: return
    val legalNames = config.legals.map { getString(it.nameRes) }.toTypedArray()

    @Suppress("SpreadOperator")
    val completeText = getString(legalTextRes, *legalNames)

    val spannable = SpannableString(completeText).apply {
        config.legals.forEachIndexed { index, authLegal ->
            val legalName = legalNames[index]
            addClickableSpanForWord(
                path = legalName,
                highLightColor = null,
                underline = false
            ) {
                onLegalClick.invoke(authLegal.id)
            }
        }
    }

    textView.text = spannable
    textView.movementMethod = LinkMovementMethod.getInstance()
}