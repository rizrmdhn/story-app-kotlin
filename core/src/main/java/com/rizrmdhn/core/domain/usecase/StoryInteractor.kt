package com.rizrmdhn.core.domain.usecase


import com.rizrmdhn.core.domain.repository.IStoryRepository


class StoryInteractor(
    private val storyRepository: IStoryRepository
) : StoryUseCase {
    override fun login(email: String, password: String) = storyRepository.login(email, password)

    override fun register(
        name: String,
        email: String,
        password: String
    ) = storyRepository.register(
        name,
        email,
        password
    )

    override fun getStories(
        page: Int,
        size: Int,
        location: Int,
        token: String
    ) = storyRepository.getStories(page, size, location, token)

    override fun getStoryDetail(id: String, token: String) =
        storyRepository.getStoryDetail(id, token)

    override fun getDarkMode() = storyRepository.getDarkMode()

    override suspend fun setDarkMode(isDarkMode: Boolean) = storyRepository.setDarkMode(isDarkMode)

    override fun getAccessToken() = storyRepository.getAccessToken()

    override suspend fun setAccessToken(token: String) = storyRepository.setAccessToken(token)

    override suspend fun removeAccessToken() = storyRepository.removeAccessToken()
}