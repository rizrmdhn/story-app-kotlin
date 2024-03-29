package com.rizrmdhn.core.data.source.remote

import com.rizrmdhn.core.data.source.remote.network.ApiResponse
import com.rizrmdhn.core.data.source.remote.network.ApiService
import com.rizrmdhn.core.data.source.remote.response.AddNewStoryResponse
import com.rizrmdhn.core.data.source.remote.response.DetailStory
import com.rizrmdhn.core.data.source.remote.response.ListStoryItem
import com.rizrmdhn.core.data.source.remote.response.LoginResponse
import com.rizrmdhn.core.data.source.remote.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
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

    suspend fun getStories(
        page: Int,
        size: Int,
        location: Int,
    ): List<ListStoryItem> {
        return apiService.getStories(
            page,
            size,
            location,
        ).listStory
    }

    fun getStoriesWithLocation(): Flow<ApiResponse<List<ListStoryItem>>> {
        return flow {
            try {
                val response = apiService.getStoriesWithLocation()
                val data = response.listStory
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

    fun getStoryDetail(
        id: String,
    ): Flow<ApiResponse<DetailStory>> {
        return flow {
            try {
                val response = apiService.getStoryDetail(
                    id,
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

    fun addNewStories(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        long: RequestBody?,
    ): Flow<ApiResponse<AddNewStoryResponse>> {
        return flow {
            try {
                val response = apiService.addNewStories(
                    file,
                    description,
                    lat,
                    long,
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
}
