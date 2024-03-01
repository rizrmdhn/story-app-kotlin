package com.rizrmdhn.core.domain.usecase


import com.rizrmdhn.core.domain.repository.IStoryRepository


class StoryInteractor(
    private val storyRepository: IStoryRepository
) : StoryUseCase {
    override fun login(email: String, password: String) = storyRepository.login(email, password)

    override fun register(email: String, password: String) = storyRepository.register(email, password)

    override fun getStories(
        page: Int,
        size: Int,
        location: Int,
        token: String
    ) = storyRepository.getStories(page, size, location, token)

    override fun getStoryDetail(id: String, token: String) = storyRepository.getStoryDetail(id, token)
}