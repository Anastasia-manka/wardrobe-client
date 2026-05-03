package com.example.wardrobe_client.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothing_items")
data class ClothingItemEntity(
    @PrimaryKey val id: String,
    val imageUrl: String,
    val categoryId: String,
    val categoryName: String,
    val categoryGroupName: String,
    val seasonIds: String,
    val seasonNames: String,
    val colorId: String,
    val colorName: String,
    val materialId: String,
    val materialName: String,
    val labelsJson: String,
    val storagePlace: String,
    val comment: String
)