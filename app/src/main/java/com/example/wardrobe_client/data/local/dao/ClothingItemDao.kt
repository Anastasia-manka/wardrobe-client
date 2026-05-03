package com.example.wardrobe_client.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wardrobe_client.data.local.entity.ClothingItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClothingItemDao {
    @Query("SELECT * FROM clothing_items")
    fun getAllItems(): Flow<List<ClothingItemEntity>>

    @Query("SELECT * FROM clothing_items WHERE id = :id")
    suspend fun getItemById(id: String): ClothingItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ClothingItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ClothingItemEntity)

    @Query("DELETE FROM clothing_items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM clothing_items")
    suspend fun deleteAll()
}