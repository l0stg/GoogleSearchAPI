package com.mintrocket.data.api

import com.mintrocket.data.model.domain.ContentPost
import com.mintrocket.datacore.model.PaginationPage
import com.mintrocket.data.model.network.auth.AuthToken
import com.mintrocket.data.model.network.auth.CheckCodeRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

private const val API_VERSION = "/api/v1"

interface ApplicationApi {

    @GET("$API_VERSION/mainpage/index")
    suspend fun getPosts(
        @Query("page") page: Int,
        @Query("per_page") pageSize: Int
    ): PaginationPage<ContentPost>

    @POST("$API_VERSION/auth/check_code")
    suspend fun checkCode(
        @Body request: CheckCodeRequest
    ): AuthToken

    @POST("$API_VERSION/auth/send_code")
    fun sendCode(
        @Body phone: String
    ): Call<Unit>

    @POST("$API_VERSION/auth/refresh_token")
    suspend fun refreshToken(
        @Body token: String
    ): AuthToken
}