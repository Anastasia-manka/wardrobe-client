package com.example.wardrobe_client.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LabelDto(
    val id: String,
    val name: String,
    val isCustom: Boolean
)

@Serializable
data class ClothingItemDto(
    val id: String,
    val userId: String? = null,
    val imageUrl: String,
    val categoryId: String,
    val categoryName: String? = null,
    val categoryGroupName: String? = null,
    val seasonId: String? = null,
    val seasonIds: List<String>? = null,
    val seasonNames: List<String>? = null,
    val colorId: String,
    val colorName: String? = null,
    val materialId: String,
    val materialName: String? = null,
    val labels: List<LabelDto>? = null,
    val labelIds: List<String>? = null,
    val storagePlace: String? = null,
    val comment: String? = null
)
@Serializable
data class CreateClothingItemRequestDto(
    val imageUrl: String,
    val categoryId: String,
    val seasonId: String,
    val colorId: String,
    val materialId: String,
    val labelIds: List<String>,
    val storagePlace: String,
    val comment: String
)

@Serializable
data class TemplateItemDto(
    val id: String,
    val imageUrl: String,
    val categoryId: String,
    val categoryName: String? = null,
    val seasonId: String,
    val colorId: String,
    val colorName: String? = null,
    val materialId: String
)

@Serializable
data class CreateFromTemplatesRequestDto(
    val templateIds: List<String>
)