package com.example.wardrobe_client.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReferenceItemDto(
    val id: String,
    val name: String
)

@Serializable
data class CategoryGroupDto(
    val id: String,
    val name: String,
    val categories: List<ReferenceItemDto>
)

@Serializable
data class ReferencesDto(
    val categoryGroups: List<CategoryGroupDto>,
    val seasons: List<ReferenceItemDto>,
    val colors: List<ReferenceItemDto>,
    val materials: List<ReferenceItemDto>,
    val labels: List<ReferenceItemDto>,
    val styles: List<ReferenceItemDto>,
    val tripTypes: List<ReferenceItemDto>,
    val climates: List<ReferenceItemDto>,
    val activities: List<ReferenceItemDto>,
    val luggageTypes: List<ReferenceItemDto>
)