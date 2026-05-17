package com.example.wardrobe_client.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class VisualSearchResultDto(
    val id: String,
    val userId: String? = null,
    val imageUrl: String,
    val categoryId: String,
    val seasonId: String,
    val colorId: String,
    val materialId: String,
    val storagePlace: String? = null,
    val comment: String? = null,
    val labels: List<String> = emptyList()
)

@Serializable
data class VisualSearchGroupDto(
    val categoryGroup: String,
    val confidence: Float,
    val items: List<VisualSearchResultDto>
)

@Serializable
data class VisualSearchResponseDto(
    val grouped: Boolean,
    val items: List<VisualSearchResultDto>,
    val groups: List<VisualSearchGroupDto>
)