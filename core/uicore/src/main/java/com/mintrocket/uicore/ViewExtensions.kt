package com.mintrocket.uicore

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.text.Spannable
import android.text.TextPaint
import android.text.TextWatcher
import android.text.style.ClickableSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import timber.log.Timber
import kotlin.math.roundToInt

fun TextView.setDrawableLeft(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
}

fun TextView.setDrawableRight(drawable: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0)
}

fun TextView.setTextIfFit(
    desiredText: String,
    fallback: String
) {
    val rect = Rect()
    paint.getTextBounds(desiredText, 0, desiredText.length, rect)
    val fit =
        width > Math.abs(rect.width()) + 2.dpToPx // by some reason calculated size less than real
    text = if (fit) desiredText else fallback
}

fun TextView.setTextColorRes(id: Int) {
    val color = ContextCompat.getColor(this.context, id)
    this.setTextColor(color)
}

fun EditText.applyText(text: String) {
    setText(text)
    setSelection(text.length)
}

fun Spannable.addSpanForWord(path: String, span: Any): Spannable {
    val startIndexOfPath = toString().indexOf(path)
    setSpan(
        span, startIndexOfPath,
        startIndexOfPath + path.length, 0
    )
    return this
}

fun Spannable.addClickableSpanForWord(
    path: String,
    @ColorInt highLightColor: Int?,
    underline: Boolean,
    onClick: () -> Unit
) {
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            onClick.invoke()
        }


        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            if (highLightColor != null) {
                ds.color = highLightColor
            }
            ds.isUnderlineText = underline
        }
    }

    addSpanForWord(path, clickableSpan)
}

fun CollapsingToolbarLayout.isCollapsed(
    verticalOffset: Int
) = height + verticalOffset < scrimVisibleHeightTrigger

fun View.setAlphaBackground(color: Int, fraction: Float) {
    setBackgroundColor(color.adjustAlpha(fraction))
}

fun View.getBottomOnScreen(): Int {
    val locations = IntArray(2)
    getLocationOnScreen(locations)
    return locations[1]
}

@ColorInt
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = (Color.alpha(this) * factor).roundToInt()
    val red: Int = Color.red(this)
    val green: Int = Color.green(this)
    val blue: Int = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}

fun LifecycleOwner.manageProgressWithRefresh(
    show: Boolean,
    progressView: View,
    refresh: SwipeRefreshLayout
) {
    progressView.isVisible = show && !refresh.isRefreshing
    if (!show) {
        refresh.isRefreshing = false
    }
}

fun View.listenKeyboardMargin() {
    ViewCompat.setOnApplyWindowInsetsListener(this) { _, insets ->
        val bottomInsets = insets.systemWindowInsetBottom
        Timber.d("TESTING insetsListener bottom insets $bottomInsets")
        (layoutParams as? CoordinatorLayout.LayoutParams)?.let {
            it.bottomMargin = bottomInsets
            this.layoutParams = it
        }
        insets
    }
}

fun View.subscribeToKeyboardWithInsetsCatcher(catcher: View) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bottomInsets = insets.systemWindowInsetBottom
        (catcher.layoutParams as? LinearLayout.LayoutParams)?.let {
            it.height = bottomInsets
            catcher.layoutParams = it
        }
        insets
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}

fun Activity.hideKeyboard() {
    currentFocus?.hideKeyboard()
}

fun View.hideKeyboard() {
    clearFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsetsCompat, ViewPaddingState) -> Unit) {
    // Create a snapshot of the view's padding state
    val paddingState = createStateForView(this)
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        f(v, insets, paddingState)
        insets
    }
    requestApplyInsetsWhenAttached()
}

fun View.requestApplyInsetsWhenAttached() {
    if (isAttachedToWindow) {
        requestApplyInsets()
    } else {
        addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                v.requestApplyInsets()
            }

            override fun onViewDetachedFromWindow(v: View) = Unit
        })
    }
}

private fun createStateForView(view: View) = ViewPaddingState(
    view.paddingLeft,
    view.paddingTop, view.paddingRight, view.paddingBottom, view.paddingStart, view.paddingEnd
)

data class ViewPaddingState(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
    val start: Int,
    val end: Int
)

fun EditText.updateTextField(text: CharSequence?, watcher: TextWatcher?) {
    val field = this
    if (field.text?.toString().orEmpty() == text) {
        return
    }
    if (watcher != null) {
        field.removeTextChangedListener(watcher)
    }
    var selectionStart = field.selectionStart
    var selectionEnd = field.selectionStart

    //Устанавливем курсор в конец текста, если поле было пустым
    val isFieldWasEmpty = field.text.isNullOrEmpty() && !text.isNullOrEmpty()
    val isSelectionOnStart = selectionStart == 0 && selectionEnd == 0
    if (isFieldWasEmpty && isSelectionOnStart) {
        selectionStart = text?.length ?: 0
        selectionEnd = selectionStart
    }
    field.setText(text)
    val range = 0..field.length()
    if (!range.isEmpty()) {
        selectionStart = selectionStart.coerceIn(range)
        selectionEnd = selectionEnd.coerceIn(range)
    }
    field.setSelection(selectionStart, selectionEnd)
    if (watcher != null) {
        field.addTextChangedListener(watcher)
    }
}

fun View.onClick(function: () -> Unit) {
    setOnClickListener {
        function()
    }
}