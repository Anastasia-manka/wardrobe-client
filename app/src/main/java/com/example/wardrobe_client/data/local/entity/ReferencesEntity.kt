package com.example.wardrobe_client.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "references_cache")
data class ReferencesEntity(
    @PrimaryKey val id: Int = 1,
    val json: String
)