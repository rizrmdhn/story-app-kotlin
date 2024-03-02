package com.rizrmdhn.core.domain.usecase

import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.data.source.remote.response.AddNewStoryResponse
import com.rizrmdhn.core.data.source.remote.response.LoginResponse
import com.rizrmdhn.core.data.source.remote.response.RegisterResponse
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.domain.model.StoryDetails
import kotlinx.coroutines.flow.Flow
import java.io.File

interface StoryUseCase {
    fun login(email: String, password: String): Flow<Resource<LoginResponse>>

    fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Resource<RegisterResponse>>

    fun getStories(page: Int, size: Int, location: Int, token: String): Flow<Resource<List<Story>>>

    fun getStoryDetail(id: String, token: String): Flow<Resource<StoryDetails>>

    fun addNewStory(
        file: File,
        description: String,
        lat: Double?,
        long: Double?,
        token: String
    ): Flow<Resource<AddNewStoryResponse>>

    fun getDarkMode(): Flow<Boolean>

    suspend fun setDarkMode(isDarkMode: Boolean)

    fun getAccessToken(): Flow<String>

    suspend fun setAccessToken(token: String)

    suspend fun removeAccessToken()

    fun getLocaleSetting(): Flow<String>

    suspend fun setLocaleSetting(locale: String)
}