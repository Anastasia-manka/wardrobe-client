package com.example.wardrobe_client.domain.repository

import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.model.OutfitItem

interface OutfitRepository {
    suspend fun getOutfits(): Result<List<Outfit>>
    suspend fun getOutfit(id: String): Result<Outfit>
    suspend fun createOutfit(coverUrl: String, styleId: String, items: List<OutfitItem>): Result<Outfit>
    suspend fun updateOutfit(id: String, coverUrl: String, styleId: String, items: List<OutfitItem>): Result<Outfit>
    suspend fun deleteOutfit(id: String): Result<Unit>
}