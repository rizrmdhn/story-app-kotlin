package com.rizrmdhn.core.data.source.remote.response

data class GetAllStoriesResponse(
	val listStory: List<ListStoryItem>,
	val error: Boolean,
	val message: String
)

data class ListStoryItem(
	val photoUrl: String,
	val createdAt: String,
	val name: String,
	val description: String,
	val lon: Any,
	val id: String,
	val lat: Any
)

