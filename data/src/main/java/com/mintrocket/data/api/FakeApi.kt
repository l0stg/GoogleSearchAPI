package com.mintrocket.data.api

import android.content.Context
import android.widget.Toast
import com.mintrocket.data.model.domain.ContentPost
import com.mintrocket.data.model.network.auth.AuthToken
import com.mintrocket.data.model.network.auth.CheckCodeRequest
import com.mintrocket.datacore.errorhandling.WrongCodeException
import com.mintrocket.datacore.model.PaginationMeta
import com.mintrocket.datacore.model.PaginationPage
import kotlinx.coroutines.delay
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Class for demo purposes. Just remove it and replace with implementation created with the retrofit
 */
class FakeApi(
    private val context: Context
) : ApplicationApi {

    companion object {
        private const val FAKE_EXPIRES_IN = 3600L
        private const val MAX_PAGE = 5
        private const val DELAY_MS = 1200L
    }

    override suspend fun getPosts(page: Int, pageSize: Int): PaginationPage<ContentPost> {
        delay(DELAY_MS)
        return if (page > MAX_PAGE) {
            PaginationPage(emptyList(), PaginationMeta(page, MAX_PAGE, pageSize, pageSize * MAX_PAGE))
        } else {
            val items = (0 until pageSize).map { ContentPost(it + ((page - 1) * pageSize)) }
            PaginationPage(items, PaginationMeta(page, MAX_PAGE, pageSize, pageSize * MAX_PAGE))
        }
    }

    private var code: String? = null

    override suspend fun checkCode(request: CheckCodeRequest): AuthToken {
        if (request.code == code) {
            return AuthToken(
                "token",
                "refresh",
                "Bearer",
                FAKE_EXPIRES_IN
            )
        } else throw WrongCodeException(RuntimeException())
    }

    override suspend fun refreshToken(token: String): AuthToken {
        TODO("Not yet implemented")
    }

    override fun sendCode(phone: String): Call<Unit> {
        code = "34345"
        Toast.makeText(context, "Code is 34345", Toast.LENGTH_LONG)
            .show()

        return object : Call<Unit> {
            override fun clone(): Call<Unit> {
                return this
            }

            override fun execute(): Response<Unit> {
                return Response.success(Unit)
            }

            override fun enqueue(callback: Callback<Unit>) {
                callback.onResponse(
                    this,
                    Response.success(Unit)
                )
            }

            override fun isExecuted(): Boolean {
                TODO("Not yet implemented")
            }

            override fun cancel() {
                TODO("Not yet implemented")
            }

            override fun isCanceled(): Boolean {
                TODO("Not yet implemented")
            }

            override fun request(): Request {
                TODO("Not yet implemented")
            }

            override fun timeout(): Timeout {
                TODO("Not yet implemented")
            }
        }
    }
}