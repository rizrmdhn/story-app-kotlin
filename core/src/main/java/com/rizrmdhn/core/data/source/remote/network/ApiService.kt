package com.rizrmdhn.core.data.source.remote.network

import com.rizrmdhn.core.data.source.remote.response.GetAllStoriesResponse
import com.rizrmdhn.core.data.source.remote.response.GetDetailResponse
import com.rizrmdhn.core.data.source.remote.response.LoginResponse
import com.rizrmdhn.core.data.source.remote.response.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("register")
    suspend fun register(
        @Body body: Map<String, String>
    ): RegisterResponse

    @Multipart
    @POST("login")
    suspend fun login(
        @Body body: Map<String, String>
    ): LoginResponse

    @Multipart
    @POST("stories")
    suspend fun addNewStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody?,
        @Part("lon") long: RequestBody?,
        @Header("Authorization") token: String
    ): RegisterResponse

    @GET("stories")
    suspend fun getStories(
        @Path("page") page: Int,
        @Path("size") size: Int,
        @Path("location") location: Int,
        @Header("Authorization") token: String
    ): GetAllStoriesResponse

    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String,
        @Header("Authorization") token: String
    ): GetDetailResponse
}