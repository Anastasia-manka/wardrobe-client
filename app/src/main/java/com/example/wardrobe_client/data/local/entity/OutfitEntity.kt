package com.example.wardrobe_client.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outfits")
data class OutfitEntity(
    @PrimaryKey val id: String,
    val coverUrl: String,
    val styleId: String,
    val styleName: String,
    val itemsJson: String
)