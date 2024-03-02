package com.rizrmdhn.core.utils

import com.rizrmdhn.core.data.source.local.entity.StoryEntity
import com.rizrmdhn.core.data.source.remote.response.DetailStory
import com.rizrmdhn.core.data.source.remote.response.ListStoryItem
import com.rizrmdhn.core.domain.model.Story
import com.rizrmdhn.core.domain.model.StoryDetails

object DataMapper {
    fun mapResponseToEntities(input: List<ListStoryItem>): List<StoryEntity> {
        val storyList = ArrayList<StoryEntity>()
        input.map {
            val story = StoryEntity(
                id = it.id,
                createdAt = it.createdAt,
                name = it.name,
                description = it.description,
                lat = it.lat,
                lon = it.lon,
                photoUrl = it.photoUrl,
            )
            storyList.add(story)
        }
        return storyList
    }

    fun mapEntitiesToDomain(input: List<StoryEntity>): List<Story> =
        input.map {
            Story(
                id = it.id,
                createdAt = it.createdAt,
                name = it.name,
                description = it.description,
                lat = it.lat,
                lon = it.lon,
                photoUrl = it.photoUrl,
            )
        }

    fun mapDomainToEntity(input: Story) = StoryEntity(
        id = input.id,
        createdAt = input.createdAt,
        name = input.name,
        description = input.description,
        lat = input.lat,
        lon = input.lon,
        photoUrl = input.photoUrl,
    )

    fun mapStoryDetailToDomain(input: DetailStory) = StoryDetails(
        id = input.id,
        createdAt = input.createdAt,
        name = input.name,
        description = input.description,
        lat = input.lat,
        lon = input.lon,
        photoUrl = input.photoUrl,
    )
}