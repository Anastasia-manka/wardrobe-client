package com.example.wardrobe_client.domain.model

data class Trip(
    val id: String,
    val name: String,
    val tripDate: String,
    val tripTypeId: String,
    val tripTypeName: String,
    val climateId: String,
    val climateName: String,
    val luggageTypeId: String,
    val luggageTypeName: String,
    val activities: List<ReferenceItem>,
    val items: List<TripItem>
)

data class TripItem(
    val id: String,
    val itemId: String,
    val imageUrl: String,
    val categoryName: String,
    val isPacked: Boolean
)