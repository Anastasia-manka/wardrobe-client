package com.example.wardrobe_client.domain.repository

import com.example.wardrobe_client.domain.model.ClothingItem
import java.io.File

interface VisualSearchRepository {
    suspend fun searchByImage(imageFile: File): List<ClothingItem>
}