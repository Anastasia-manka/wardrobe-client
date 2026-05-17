package com.example.wardrobe_client.domain.repository

import com.example.wardrobe_client.domain.model.ClothingItem
import java.io.File

data class VisualSearchResult(
    val grouped: Boolean,
    val items: List<ClothingItem>,
    val groups: List<VisualSearchGroup>
)

data class VisualSearchGroup(
    val categoryGroup: String,
    val confidence: Float,
    val items: List<ClothingItem>
)

interface VisualSearchRepository {
    suspend fun searchByImage(imageFile: File): VisualSearchResult
}