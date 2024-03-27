package com.rizrmdhn.storyapp.utils

import com.rizrmdhn.core.domain.model.Story

object DummyData {
    fun generateDummyStory(): List<Story> {
        val items: MutableList<Story> = arrayListOf()

        for (i in 0..10) {
            val story = Story(
                id = "id_$i",
                name = "Title $i",
                lat = 0.0,
                lon = 0.0,
                createdAt = "2021-08-01T00:00:00Z",
                description = "Description $i",
                photoUrl = "https://via.placeholder.com/600/92c952",
            )
            items.add(story)
        }
        return items
    }

    fun generateDummyStoryDetails(): Story {
        return Story(
            id = "id_1",
            name = "Title 1",
            lat = 0.0,
            lon = 0.0,
            createdAt = "2021-08-01T00:00:00Z",
            description = "Description 1",
            photoUrl = "https://via.placeholder.com/600/92c952",
        )
    }
}