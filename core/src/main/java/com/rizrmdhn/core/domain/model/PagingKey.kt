package com.rizrmdhn.core.domain.model

data class PagingKey(
    val page: Int,
    val location: Int,
    val token: String
)