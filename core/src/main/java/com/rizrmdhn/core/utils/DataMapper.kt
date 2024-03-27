package com.rizrmdhn.core.utils

import androidx.paging.PagingData
import androidx.paging.map
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

    fun mapStoryDetailToDomain(input: DetailStory) = StoryDetails(
        id = input.id,
        createdAt = input.createdAt,
        name = input.name,
        description = input.description,
        lat = input.lat,
        lon = input.lon,
        photoUrl = input.photoUrl,
    )

    fun mapStoryToStoryDetails(input: Story) = StoryDetails(
        id = input.id,
        createdAt = input.createdAt,
        name = input.name,
        description = input.description,
        lat = input.lat,
        lon = input.lon,
        photoUrl = input.photoUrl,
    )
}