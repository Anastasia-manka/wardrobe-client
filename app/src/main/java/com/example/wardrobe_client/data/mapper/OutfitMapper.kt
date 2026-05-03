package com.example.wardrobe_client.data.mapper

import com.example.wardrobe_client.data.local.entity.OutfitEntity
import com.example.wardrobe_client.data.remote.dto.OutfitDto
import com.example.wardrobe_client.data.remote.dto.OutfitItemDto
import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.model.OutfitItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun OutfitItemDto.toDomain() = OutfitItem(
    itemId = itemId,
    x = x,
    y = y,
    scale = scale
)

fun OutfitDto.toDomain() = Outfit(
    id = id,
    coverUrl = coverUrl,
    styleId = styleId,
    styleName = styleName,
    items = items.map { it.toDomain() }
)

fun OutfitDto.toEntity(): OutfitEntity {
    val json = Json { ignoreUnknownKeys = true }
    return OutfitEntity(
        id = id,
        coverUrl = coverUrl,
        styleId = styleId,
        styleName = styleName,
        itemsJson = json.encodeToString(items)
    )
}

fun OutfitEntity.toDomain(): Outfit {
    val json = Json { ignoreUnknownKeys = true }
    val items = json.decodeFromString<List<OutfitItemDto>>(itemsJson)
    return Outfit(
        id = id,
        coverUrl = coverUrl,
        styleId = styleId,
        styleName = styleName,
        items = items.map { it.toDomain() }
    )
}