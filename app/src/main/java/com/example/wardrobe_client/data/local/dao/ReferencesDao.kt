package com.example.wardrobe_client.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wardrobe_client.data.local.entity.ReferencesEntity

@Dao
interface ReferencesDao {
    @Query("SELECT * FROM references_cache WHERE id = 1")
    suspend fun getReferences(): ReferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(references: ReferencesEntity)

    @Query("DELETE FROM references_cache")
    suspend fun deleteAll()
}