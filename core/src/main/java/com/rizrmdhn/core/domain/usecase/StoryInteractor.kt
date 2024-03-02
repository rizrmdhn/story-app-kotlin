package com.rizrmdhn.core.domain.usecase


import com.rizrmdhn.core.domain.repository.IStoryRepository
import java.io.File


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

    override fun addNewStory(
        file: File,
        description: String,
        lat: Double?,
        long: Double?,
        token: String
    ) = storyRepository.addNewStory(
        file,
        description,
        lat,
        long,
        token
    )

    override fun getDarkMode() = storyRepository.getDarkMode()

    override suspend fun setDarkMode(isDarkMode: Boolean) = storyRepository.setDarkMode(isDarkMode)

    override fun getAccessToken() = storyRepository.getAccessToken()

    override suspend fun setAccessToken(token: String) = storyRepository.setAccessToken(token)

    override suspend fun removeAccessToken() = storyRepository.removeAccessToken()

    override fun getLocaleSetting() = storyRepository.getLocaleSetting()

    override suspend fun setLocaleSetting(locale: String) = storyRepository.setLocaleSetting(locale)
}