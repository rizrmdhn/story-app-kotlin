package com.rizrmdhn.core.data.source.remote.response

data class GetDetailResponse(
	val error: Boolean,
	val message: String,
	val story: DetailStory
)

data class DetailStory(
	val photoUrl: String,
	val createdAt: String,
	val name: String,
	val description: String,
	val lon: Double?,
	val id: String,
	val lat: Double?
)

