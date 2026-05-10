package com.example.wardrobe_client.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class TripItemDto(
    val id: String,
    val itemId: String,
    val imageUrl: String = "",
    val categoryName: String = "",
    val isPacked: Boolean
)

@Serializable
data class TripDto(
    val id: String,
    val name: String,
    val tripDate: String,
    val tripTypeId: String,
    val tripTypeName: String = "",
    val climateId: String,
    val climateName: String = "",
    val luggageTypeId: String,
    val luggageTypeName: String = "",
    val activities: List<ReferenceItemDto> = emptyList(),
    val items: List<TripItemDto> = emptyList()
)

@Serializable
data class CreateTripRequestDto(
    val name: String,
    val tripDate: String,
    val tripTypeId: String,
    val climateId: String,
    val luggageTypeId: String,
    val activityIds: List<String>
)

@Serializable
data class AddTripItemRequestDto(
    val itemId: String
)

@Serializable
data class UpdatePackingStatusRequestDto(
    val isPacked: Boolean
)