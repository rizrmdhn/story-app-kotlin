package com.rizrmdhn.core.domain.usecase

import com.rizrmdhn.core.data.Resource
import com.rizrmdhn.core.data.source.remote.response.LoginResult
import com.rizrmdhn.core.domain.model.Story
import kotlinx.coroutines.flow.Flow

interface StoryUseCase {
    fun login(email: String, password: String): Flow<Resource<LoginResult>>

    fun register(email: String, password: String): Flow<Resource<Boolean>>

    fun getStories(page: Int, size: Int, location: Int, token: String): Flow<Resource<List<Story>>>

    fun getStoryDetail(id: Int, token: String): Flow<Resource<Story>>
}