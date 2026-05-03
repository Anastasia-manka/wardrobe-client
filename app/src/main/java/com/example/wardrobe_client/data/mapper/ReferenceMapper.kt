package com.example.wardrobe_client.data.mapper

import com.example.wardrobe_client.data.remote.dto.CategoryGroupDto
import com.example.wardrobe_client.data.remote.dto.ReferencesDto
import com.example.wardrobe_client.data.remote.dto.ReferenceItemDto
import com.example.wardrobe_client.domain.model.CategoryGroup
import com.example.wardrobe_client.domain.model.ReferenceItem
import com.example.wardrobe_client.domain.model.References

fun ReferenceItemDto.toDomain() = ReferenceItem(
    id = id,
    name = name
)

fun CategoryGroupDto.toDomain() = CategoryGroup(
    id = id,
    name = name,
    categories = categories.map { it.toDomain() }
)

fun ReferencesDto.toDomain() = References(
    categoryGroups = categoryGroups.map { it.toDomain() },
    seasons = seasons.map { it.toDomain() },
    colors = colors.map { it.toDomain() },
    materials = materials.map { it.toDomain() },
    labels = labels.map { it.toDomain() },
    styles = styles.map { it.toDomain() },
    tripTypes = tripTypes.map { it.toDomain() },
    climates = climates.map { it.toDomain() },
    activities = activities.map { it.toDomain() },
    luggageTypes = luggageTypes.map { it.toDomain() }
)