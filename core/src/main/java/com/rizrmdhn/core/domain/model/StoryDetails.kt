package com.rizrmdhn.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoryDetails(
    val photoUrl: String,
    val createdAt: String,
    val name: String,
    val description: String,
    val lon: Float,
    val id: String,
    val lat: Float
) : Parcelable