package com.example.wardrobe_client.data.mapper

import com.example.wardrobe_client.data.local.entity.OutfitEntity
import com.example.wardrobe_client.data.remote.dto.OutfitDto
import com.example.wardrobe_client.data.remote.dto.OutfitItemDto
import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.model.OutfitItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.float

private val json = Json { ignoreUnknownKeys = true }

fun OutfitItemDto.toDomain(): OutfitItem {
    val pos = json.parseToJsonElement(position).jsonObject
    return OutfitItem(
        itemId = itemId,
        x = pos["x"]?.jsonPrimitive?.float ?: 0f,
        y = pos["y"]?.jsonPrimitive?.float ?: 0f,
        scale = pos["scale"]?.jsonPrimitive?.float ?: 1f
    )
}

fun OutfitDto.toDomain() = Outfit(
    id = id,
    coverUrl = coverUrl,
    styleId = styleId,
    styleName = styleName,
    items = items.map { it.toDomain() }
)

fun OutfitDto.toEntity(): OutfitEntity {
    return OutfitEntity(
        id = id,
        coverUrl = coverUrl,
        styleId = styleId,
        styleName = styleName,
        itemsJson = json.encodeToString(items)
    )
}

fun OutfitEntity.toDomain(): Outfit {
    val items = json.decodeFromString<List<OutfitItemDto>>(itemsJson)
    return Outfit(
        id = id,
        coverUrl = coverUrl,
        styleId = styleId,
        styleName = styleName,
        items = items.map { it.toDomain() }
    )
}