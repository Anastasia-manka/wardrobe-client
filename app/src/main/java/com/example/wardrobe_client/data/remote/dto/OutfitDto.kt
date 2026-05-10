package com.example.wardrobe_client.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class OutfitItemDto(
    val itemId: String,
    val position: String
)

@Serializable
data class OutfitItemRequestDto(
    val itemId: String,
    val position: String
)

@Serializable
data class OutfitDto(
    val id: String,
    val coverUrl: String,
    val styleId: String,
    val styleName: String,
    val items: List<OutfitItemDto>
)

@Serializable
data class CreateOutfitRequestDto(
    val coverUrl: String,
    val styleId: String,
    val items: List<OutfitItemRequestDto>
)