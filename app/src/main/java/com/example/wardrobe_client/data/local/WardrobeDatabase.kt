package com.example.wardrobe_client.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wardrobe_client.data.local.dao.ClothingItemDao
import com.example.wardrobe_client.data.local.dao.OutfitDao
import com.example.wardrobe_client.data.local.dao.ReferencesDao
import com.example.wardrobe_client.data.local.dao.TripDao
import com.example.wardrobe_client.data.local.entity.ClothingItemEntity
import com.example.wardrobe_client.data.local.entity.OutfitEntity
import com.example.wardrobe_client.data.local.entity.ReferencesEntity
import com.example.wardrobe_client.data.local.entity.TripEntity

@Database(
    entities = [
        ClothingItemEntity::class,
        OutfitEntity::class,
        TripEntity::class,
        ReferencesEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class WardrobeDatabase : RoomDatabase() {
    abstract fun clothingItemDao(): ClothingItemDao
    abstract fun outfitDao(): OutfitDao
    abstract fun tripDao(): TripDao
    abstract fun referencesDao(): ReferencesDao
}