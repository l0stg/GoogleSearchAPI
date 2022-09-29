package com.mintrocket.testcore

import android.app.Application
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.ApplicationInfo
import android.content.pm.ResolveInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.test.core.app.ApplicationProvider
import io.mockk.mockkObject
import io.mockk.unmockkObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert
import org.robolectric.Shadows
import org.robolectric.shadows.ShadowDialog
import org.robolectric.shadows.ShadowPackageManager
import retrofit2.HttpException
import retrofit2.Response

fun getApp() = ApplicationProvider.getApplicationContext<Application>()

fun addDefaultResolveForIntent(
    intent: Intent,
    packageName: String = "com.example",
    activityName: String = "Example"
) {
    val applicationInfo = ApplicationInfo()
    val resolveInfo = ResolveInfo()
    applicationInfo.packageName = packageName
    resolveInfo.activityInfo = ActivityInfo()
    resolveInfo.activityInfo.applicationInfo = applicationInfo
    resolveInfo.activityInfo.name = activityName

    val packageManager: ShadowPackageManager = Shadows.shadowOf(getApp().packageManager)
    packageManager.addResolveInfoForIntent(intent, resolveInfo)
}

fun getNextStartedActivity() = Shadows.shadowOf(getApp()).nextStartedActivity

fun assertNextStartedIs(
    clazz: Class<*>,
    vararg args: Pair<String, Any>
) {
    val expected = Intent(getApp(), clazz)
    val called = getNextStartedActivity()
    Assert.assertEquals(expected.component, called.component)
    args.forEach {
        Assert.assertEquals(called.extras!!.get(it.first), it.second)
    }
}

fun withMockedObject(
    obj: Any,
    block: () -> Unit
) {
    mockkObject(obj)
    block.invoke()
    unmockkObject(obj)
}

fun assertAllEquals(vararg values: Pair<Any?, Any?>) {
    values.forEach {
        Assert.assertEquals(it.first, it.second)
    }
}

fun assertAlertDialogHasBody(text: String) {
    val dialog = ShadowDialog.getLatestDialog()
    val bodyTv = dialog.findViewById<TextView>(android.R.id.message)
    Assert.assertEquals(text, bodyTv.text)
}

fun clickAlertDialogOkButton() {
    val dialog = ShadowDialog.getLatestDialog()
    (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        .performClick()
}

fun <T> createHttpError(
    code: Int,
    message: String = "error"
): HttpException {
    return HttpException(
        Response.error<T>(
            code,
            "{\"error\":{\"message\":\"$message\"}}"
                .toResponseBody("application/json".toMediaType())
        )
    )
}