package com.example.wardrobe_client.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wardrobe_client.data.local.entity.OutfitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OutfitDao {
    @Query("SELECT * FROM outfits")
    fun getAllOutfits(): Flow<List<OutfitEntity>>

    @Query("SELECT * FROM outfits WHERE id = :id")
    suspend fun getOutfitById(id: String): OutfitEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(outfits: List<OutfitEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(outfit: OutfitEntity)

    @Query("DELETE FROM outfits WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM outfits")
    suspend fun deleteAll()
}