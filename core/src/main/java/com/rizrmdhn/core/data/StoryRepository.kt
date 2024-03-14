package com.rizrmdhn.core.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.JsonObject
import com.rizrmdhn.core.data.source.local.LocalDataSource
import com.rizrmdhn.core.data.source.remote.RemoteDataSource
import com.rizrmdhn.core.data.source.remote.network.ApiResponse
import com.rizrmdhn.core.data.source.remote.response.AddNewStoryResponse
import com.rizrmdhn.core.data.source.remote.response.LoginResponse
import com.rizrmdhn.core.data.source.remote.response.RegisterResponse
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.domain.model.StoryDetails
import com.rizrmdhn.core.domain.repository.IStoryRepository
import com.rizrmdhn.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : IStoryRepository {

    override fun login(email: String, password: String): Flow<Resource<LoginResponse>> {
        return flow {
            emit(Resource.Loading())
            val body = JsonObject().apply {
                addProperty("email", email)
                addProperty("password", password)
            }.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            remoteDataSource.login(body).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val loginResult = apiResponse.data
                        emit(Resource.Success(loginResult))
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Error("Empty Data"))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }
    }

    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<RegisterResponse>> {
        return flow {
            emit(Resource.Loading())
            val body = JsonObject().apply {
                addProperty("name", email)
                addProperty("email", email)
                addProperty("password", password)
            }.toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            remoteDataSource.register(body).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val result = apiResponse.data
                        emit(Resource.Success(result))
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Error("Empty Data"))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalPagingApi::class)
    override fun getStories(
        page: Int,
        location: Int,
        token: String
    ): Flow<PagingData<Story>> {
        return flow {
            try {
                val pager = Pager(
                    config = PagingConfig(
                        pageSize = 5,
                        enablePlaceholders = false
                        ),
                    remoteMediator = StoryPagingSource(
                        localDataSource,
                        remoteDataSource,
                        location,
                        token
                    ),
                    pagingSourceFactory = {
                        localDataSource.getAllStories()
                    }
                ).flow
                pager.collect {
                    emit(it)
                }
            } catch (e: Exception) {
                Log.e("StoryRepository", "getStories: ${e.message}")
            }
        }
    }

    override fun getStoryDetail(id: String, token: String): Flow<Resource<StoryDetails>> {
        return flow {
            emit(Resource.Loading())
            remoteDataSource.getStoryDetail(id, token).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val storyDetail = DataMapper.mapStoryDetailToDomain(apiResponse.data)
                        emit(Resource.Success(storyDetail))
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Error("Empty Data"))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }
    }

    override fun addNewStory(
        file: File,
        description: String,
        lat: Double?,
        long: Double?,
        token: String
    ): Flow<Resource<AddNewStoryResponse>> {
        return flow {
            emit(Resource.Loading())
            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestLat = lat?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestLong = long?.toString()?.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestImageFile = file.asRequestBody("image/*".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            remoteDataSource.addNewStories(
                multipartBody,
                requestDescription,
                requestLat,
                requestLong,
                token
            ).collect { apiResponse ->
                when (apiResponse) {
                    is ApiResponse.Success -> {
                        val result = apiResponse.data
                        emit(Resource.Success(result))
                    }

                    is ApiResponse.Empty -> {
                        emit(Resource.Error("Empty Data"))
                    }

                    is ApiResponse.Error -> {
                        emit(Resource.Error(apiResponse.errorMessage))
                    }
                }
            }
        }
    }

    override fun getDarkMode(): Flow<Boolean> {
        return localDataSource.getThemeSetting()
    }

    override suspend fun setDarkMode(isDarkMode: Boolean) {
        localDataSource.saveThemeSetting(isDarkMode)
    }

    override fun getAccessToken(): Flow<String> {
        return localDataSource.getAccessToken()
    }

    override suspend fun setAccessToken(token: String) {
        localDataSource.saveAccessToken(token)
    }

    override suspend fun removeAccessToken() {
        localDataSource.removeAccessToken()
    }

    override fun getLocaleSetting(): Flow<String> {
        return localDataSource.getLocaleSetting()
    }

    override suspend fun setLocaleSetting(locale: String) {
        localDataSource.saveLocaleSetting(locale)
    }

    override fun getLocationSetting(): Flow<Int> {
        return localDataSource.getLocationSetting()
    }

    override suspend fun setLocationSetting(location: Int) {
        localDataSource.saveLocationSetting(location)
    }
}