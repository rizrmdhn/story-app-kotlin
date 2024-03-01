package com.rizrmdhn.core.data.source.remote

import com.rizrmdhn.core.data.source.remote.network.ApiResponse
import com.rizrmdhn.core.data.source.remote.network.ApiService
import com.rizrmdhn.core.data.source.remote.response.ListStoryItem
import com.rizrmdhn.core.data.source.remote.response.LoginResult
import com.rizrmdhn.core.data.source.remote.response.DetailStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class RemoteDataSource(
    private val apiService: ApiService,
) {

    fun login(
        body: Map<String, String>
    ): Flow<ApiResponse<LoginResult>> {
        return flow {
            try {
                val response = apiService.login(
                    body
                )
                val data = response.loginResult
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    fun register(
        body: Map<String, String>
    ): Flow<ApiResponse<Boolean>> {
        return flow {
            try {
                val response = apiService.register(
                    body
                )
                val data = response.error
                emit(ApiResponse.Success(data))
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
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
                emit(ApiResponse.Error(e.toString()))
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
                emit(ApiResponse.Error(e.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}