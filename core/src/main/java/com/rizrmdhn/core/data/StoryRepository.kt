package com.rizrmdhn.core.data

import com.rizrmdhn.core.data.source.local.LocalDataSource
import com.rizrmdhn.core.data.source.remote.RemoteDataSource
import com.rizrmdhn.core.data.source.remote.network.ApiResponse
import com.rizrmdhn.core.data.source.remote.response.ListStoryItem
import com.rizrmdhn.core.data.source.remote.response.LoginResult
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.domain.model.StoryDetails
import com.rizrmdhn.core.domain.repository.IStoryRepository
import com.rizrmdhn.core.utils.AppExecutors
import com.rizrmdhn.core.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class StoryRepository(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) : IStoryRepository {

    override fun login(email: String, password: String): Flow<Resource<LoginResult>> {
        return flow {
            emit(Resource.Loading())
            val body = mapOf(
                "email" to email,
                "password" to password
            )
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

    override fun register(email: String, password: String): Flow<Resource<Boolean>> {
        return flow {
            emit(Resource.Loading())
            val body = mapOf(
                "email" to email,
                "password" to password
            )
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


    override fun getStories(
        page: Int,
        size: Int,
        location: Int,
        token: String
    ): Flow<Resource<List<Story>>> {
        return object : NetworkBoundResource<List<Story>, List<ListStoryItem>>() {
            override fun shouldFetch(data: List<Story>?): Boolean = data.isNullOrEmpty()

            override fun loadFromDB(): Flow<List<Story>> {
                return localDataSource.getAllStories().map {
                    DataMapper.mapEntitiesToDomain(it)
                }
            }

            override suspend fun createCall(): Flow<ApiResponse<List<ListStoryItem>>> {
                return remoteDataSource.getStories(page, size, location, token)
            }

            override suspend fun saveCallResult(data: List<ListStoryItem>) {
                val storyList = DataMapper.mapResponseToEntities(data)
                localDataSource.insertStories(storyList)
            }
        }.asFlow()
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
}