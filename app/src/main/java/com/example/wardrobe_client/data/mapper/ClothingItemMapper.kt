package com.example.wardrobe_client.data.mapper

import com.example.wardrobe_client.data.local.entity.ClothingItemEntity
import com.example.wardrobe_client.data.remote.dto.ClothingItemDto
import com.example.wardrobe_client.data.remote.dto.LabelDto
import com.example.wardrobe_client.data.remote.dto.TemplateItemDto
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.Label
import com.example.wardrobe_client.domain.model.TemplateItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun LabelDto.toDomain() = Label(
    id = id,
    name = name,
    isCustom = isCustom
)

fun ClothingItemDto.toDomain() = ClothingItem(
    id = id,
    imageUrl = imageUrl,
    categoryId = categoryId,
    categoryName = categoryName,
    categoryGroupName = categoryGroupName,
    seasonIds = seasonIds,
    seasonNames = seasonNames,
    colorId = colorId,
    colorName = colorName,
    materialId = materialId,
    materialName = materialName,
    labels = labels.map { it.toDomain() },
    storagePlace = storagePlace,
    comment = comment
)

fun TemplateItemDto.toDomain() = TemplateItem(
    id = id,
    imageUrl = imageUrl,
    categoryId = categoryId,
    categoryName = categoryName ?: "",
    seasonId = seasonId,
    colorId = colorId,
    colorName = colorName ?: "",
    materialId = materialId
)

fun ClothingItemDto.toEntity(): ClothingItemEntity {
    val json = Json { ignoreUnknownKeys = true }
    return ClothingItemEntity(
        id = id,
        imageUrl = imageUrl,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryGroupName = categoryGroupName,
        seasonIds = seasonIds.joinToString(","),
        seasonNames = seasonNames.joinToString(","),
        colorId = colorId,
        colorName = colorName,
        materialId = materialId,
        materialName = materialName,
        labelsJson = json.encodeToString(labels),
        storagePlace = storagePlace,
        comment = comment
    )
}

fun ClothingItemEntity.toDomain(): ClothingItem {
    val json = Json { ignoreUnknownKeys = true }
    val labels = json.decodeFromString<List<LabelDto>>(labelsJson)
    return ClothingItem(
        id = id,
        imageUrl = imageUrl,
        categoryId = categoryId,
        categoryName = categoryName,
        categoryGroupName = categoryGroupName,
        seasonIds = if (seasonIds.isEmpty()) emptyList() else seasonIds.split(","),
        seasonNames = if (seasonNames.isEmpty()) emptyList() else seasonNames.split(","),
        colorId = colorId,
        colorName = colorName,
        materialId = materialId,
        materialName = materialName,
        labels = labels.map { it.toDomain() },
        storagePlace = storagePlace,
        comment = comment
    )
}