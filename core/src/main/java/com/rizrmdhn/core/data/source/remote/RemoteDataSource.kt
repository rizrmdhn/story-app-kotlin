package com.rizrmdhn.core.data.source.remote

import android.util.Log
import com.google.gson.JsonObject
import com.rizrmdhn.core.data.source.remote.network.ApiResponse
import com.rizrmdhn.core.data.source.remote.network.ApiService
import com.rizrmdhn.core.data.source.remote.response.ListStoryItem
import com.rizrmdhn.core.data.source.remote.response.DetailStory
import com.rizrmdhn.core.data.source.remote.response.LoginResponse
import com.rizrmdhn.core.data.source.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException


class RemoteDataSource(
    private val apiService: ApiService,
) {

    fun login(
        body: RequestBody
    ): Flow<ApiResponse<LoginResponse>> {
        return flow {
            try {
                val response = apiService.login(
                    body
                )
                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                } else {
                    emit(ApiResponse.Success(response))
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    val exception: HttpException = e
                    val response = exception.response()
                    try {
                        val jsonObject = JSONObject(response?.errorBody()?.string() ?: "Error")
                        emit(ApiResponse.Error(jsonObject.optString("message")))
                    } catch (e1: JSONException) {
                        e1.printStackTrace()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                } else {
                    emit(ApiResponse.Error(e.toString()))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun register(
        body: RequestBody
    ): Flow<ApiResponse<RegisterResponse>> {
        return flow {
            try {
                val response = apiService.register(
                    body
                )
                if (response.error) {
                    emit(ApiResponse.Error(response.message))
                } else {
                    emit(ApiResponse.Success(response))
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    val exception: HttpException = e
                    val response = exception.response()
                    try {
                        val jsonObject = JSONObject(response?.errorBody()?.string() ?: "Error")
                        emit(ApiResponse.Error(jsonObject.optString("message")))
                    } catch (e1: JSONException) {
                        e1.printStackTrace()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                } else {
                    emit(ApiResponse.Error(e.toString()))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getStories(
        page: Int,
        size: Int,
        location: Int,
        token: String
    ): Flow<ApiResponse<List<ListStoryItem>>> {
        return flow {
            try {
                val response = apiService.getStories(
                    page,
                    size,
                    location,
                    token
                )
                val dataArray = response.listStory
                if (dataArray.isNotEmpty()) {
                    emit(ApiResponse.Success(dataArray))
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                if (e is HttpException) {
                    val exception: HttpException = e
                    val response = exception.response()
                    try {
                        val jsonObject = JSONObject(response?.errorBody()?.string() ?: "Error")
                        emit(ApiResponse.Error(jsonObject.optString("message")))
                    } catch (e1: JSONException) {
                        e1.printStackTrace()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                } else {
                    emit(ApiResponse.Error(e.toString()))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun getStoryDetail(
        id: String,
        token: String
    ): Flow<ApiResponse<DetailStory>> {
        return flow {
            try {
                val response = apiService.getStoryDetail(
                    id,
                    token
                )
                val data = response.story
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                if (e is HttpException) {
                    val exception: HttpException = e
                    val response = exception.response()
                    try {
                        val jsonObject = JSONObject(response?.errorBody()?.string() ?: "Error")
                        emit(ApiResponse.Error(jsonObject.optString("message")))
                    } catch (e1: JSONException) {
                        e1.printStackTrace()
                    } catch (e1: IOException) {
                        e1.printStackTrace()
                    }
                } else {
                    emit(ApiResponse.Error(e.toString()))
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}