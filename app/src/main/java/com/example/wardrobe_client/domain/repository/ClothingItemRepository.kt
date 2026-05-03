package com.example.wardrobe_client.domain.repository

import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.TemplateItem

interface ClothingItemRepository {
    suspend fun getItems(
        categoryId: String? = null,
        seasonId: String? = null,
        colorId: String? = null,
        materialId: String? = null,
        labelId: String? = null
    ): Result<List<ClothingItem>>
    suspend fun getItem(id: String): Result<ClothingItem>
    suspend fun createItem(item: ClothingItem, imageUrl: String): Result<ClothingItem>
    suspend fun updateItem(id: String, item: ClothingItem, imageUrl: String?): Result<ClothingItem>
    suspend fun deleteItem(id: String): Result<Unit>
    suspend fun getTemplateItems(): Result<List<TemplateItem>>
    suspend fun createItemsFromTemplates(templateIds: List<String>): Result<Unit>
    suspend fun getCompatibleItems(id: String, categoryGroupId: String?): Result<List<ClothingItem>>
    suspend fun addCompatibility(itemId: String, compatibleItemId: String): Result<Unit>
    suspend fun getItemOutfits(id: String): Result<List<com.example.wardrobe_client.domain.model.Outfit>>
}