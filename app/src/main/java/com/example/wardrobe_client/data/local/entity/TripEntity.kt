package com.example.wardrobe_client.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trips")
data class TripEntity(
    @PrimaryKey val id: String,
    val name: String,
    val tripDate: String,
    val tripTypeId: String,
    val tripTypeName: String,
    val climateId: String,
    val climateName: String,
    val luggageTypeId: String,
    val luggageTypeName: String,
    val activitiesJson: String,
    val itemsJson: String
)