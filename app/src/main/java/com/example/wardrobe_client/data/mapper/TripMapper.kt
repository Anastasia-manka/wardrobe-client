package com.example.wardrobe_client.data.mapper

import com.example.wardrobe_client.data.local.entity.TripEntity
import com.example.wardrobe_client.data.remote.dto.ReferenceItemDto
import com.example.wardrobe_client.data.remote.dto.TripDto
import com.example.wardrobe_client.data.remote.dto.TripItemDto
import com.example.wardrobe_client.domain.model.ReferenceItem
import com.example.wardrobe_client.domain.model.Trip
import com.example.wardrobe_client.domain.model.TripItem
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun TripItemDto.toDomain() = TripItem(
    id = id,
    itemId = itemId,
    isPacked = isPacked
)

fun TripDto.toDomain() = Trip(
    id = id,
    name = name,
    tripDate = tripDate,
    tripTypeId = tripTypeId,
    tripTypeName = tripTypeName,
    climateId = climateId,
    climateName = climateName,
    luggageTypeId = luggageTypeId,
    luggageTypeName = luggageTypeName,
    activities = activities.map { it.toDomain() },
    items = items.map { it.toDomain() }
)

fun TripDto.toEntity(): TripEntity {
    val json = Json { ignoreUnknownKeys = true }
    return TripEntity(
        id = id,
        name = name,
        tripDate = tripDate,
        tripTypeId = tripTypeId,
        tripTypeName = tripTypeName,
        climateId = climateId,
        climateName = climateName,
        luggageTypeId = luggageTypeId,
        luggageTypeName = luggageTypeName,
        activitiesJson = json.encodeToString(activities),
        itemsJson = json.encodeToString(items)
    )
}

fun TripEntity.toDomain(): Trip {
    val json = Json { ignoreUnknownKeys = true }
    val activities = json.decodeFromString<List<ReferenceItemDto>>(activitiesJson)
    val items = json.decodeFromString<List<TripItemDto>>(itemsJson)
    return Trip(
        id = id,
        name = name,
        tripDate = tripDate,
        tripTypeId = tripTypeId,
        tripTypeName = tripTypeName,
        climateId = climateId,
        climateName = climateName,
        luggageTypeId = luggageTypeId,
        luggageTypeName = luggageTypeName,
        activities = activities.map { it.toDomain() },
        items = items.map { it.toDomain() }
    )
}