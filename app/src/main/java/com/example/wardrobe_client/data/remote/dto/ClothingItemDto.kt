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
    val imageUrl: String,
    val categoryId: String,
    val categoryName: String,
    val categoryGroupName: String,
    val seasonIds: List<String>,
    val seasonNames: List<String>,
    val colorId: String,
    val colorName: String,
    val materialId: String,
    val materialName: String,
    val labels: List<LabelDto>,
    val storagePlace: String,
    val comment: String
)

@Serializable
data class CreateClothingItemRequestDto(
    val imageUrl: String,
    val categoryId: String,
    val seasonIds: List<String>,
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