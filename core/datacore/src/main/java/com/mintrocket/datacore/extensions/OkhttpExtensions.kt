package com.mintrocket.datacore.extensions

import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import kotlin.coroutines.resumeWithException

fun Interceptor.Chain.proceedForHost(host: String, transform: () -> Request): Response {
    return if (request().url.host == host) {
        proceed(transform())
    } else proceed(request())
}

fun Request.attachAuthToken(token: String, tokenType: String): Request = newBuilder()
    .header("Authorization", "$tokenType $token")
    .build()

fun Response.responseCount(): Int {
    var result = 1
    var response = priorResponse
    while (response != null) {
        result++
        response = response.priorResponse
    }
    return result
}

/**
 * Retrofit extension for kotlin can't handle empty responses
 */
suspend fun Call<Unit>.awaitEmpty() {
    return suspendCancellableCoroutine { continuation ->
        continuation.invokeOnCancellation { cancel() }
        enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                continuation.resumeWithException(t)
            }

            override fun onResponse(call: Call<Unit>, response: retrofit2.Response<Unit>) {
                if (response.isSuccessful) {
                    continuation.resume(Unit, {})
                } else {
                    continuation.resumeWithException(HttpException(response))
                }
            }
        })
    }
}