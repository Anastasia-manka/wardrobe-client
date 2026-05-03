package com.example.wardrobe_client.data.repository

import com.example.wardrobe_client.data.local.dao.ClothingItemDao
import com.example.wardrobe_client.data.mapper.toDomain
import com.example.wardrobe_client.data.mapper.toEntity
import com.example.wardrobe_client.data.remote.api.ClothingItemApi
import com.example.wardrobe_client.data.remote.dto.CreateClothingItemRequestDto
import com.example.wardrobe_client.data.remote.dto.CreateFromTemplatesRequestDto
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.model.TemplateItem
import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ClothingItemRepositoryImpl @Inject constructor(
    private val clothingItemApi: ClothingItemApi,
    private val clothingItemDao: ClothingItemDao
) : ClothingItemRepository {

    override suspend fun getItems(
        categoryId: String?,
        seasonId: String?,
        colorId: String?,
        materialId: String?,
        labelId: String?
    ): Result<List<ClothingItem>> = runCatching {
        try {
            val items = clothingItemApi.getItems(categoryId, seasonId, colorId, materialId, labelId)
            clothingItemDao.insertAll(items.map { it.toEntity() })
            items.map { it.toDomain() }
        } catch (e: Exception) {
            clothingItemDao.getAllItems().first().map { it.toDomain() }
        }
    }

    override suspend fun getItem(id: String): Result<ClothingItem> = runCatching {
        try {
            val item = clothingItemApi.getItem(id)
            clothingItemDao.insert(item.toEntity())
            item.toDomain()
        } catch (e: Exception) {
            clothingItemDao.getItemById(id)?.toDomain()
                ?: throw Exception("Вещь не найдена")
        }
    }

    override suspend fun createItem(item: ClothingItem, imageUrl: String): Result<ClothingItem> = runCatching {
        val request = CreateClothingItemRequestDto(
            imageUrl = imageUrl,
            categoryId = item.categoryId,
            seasonIds = item.seasonIds,
            colorId = item.colorId,
            materialId = item.materialId,
            labelIds = item.labels.map { it.id },
            storagePlace = item.storagePlace,
            comment = item.comment
        )
        val created = clothingItemApi.createItem(request)
        clothingItemDao.insert(created.toEntity())
        created.toDomain()
    }

    override suspend fun updateItem(id: String, item: ClothingItem, imageUrl: String?): Result<ClothingItem> = runCatching {
        val request = CreateClothingItemRequestDto(
            imageUrl = imageUrl ?: item.imageUrl,
            categoryId = item.categoryId,
            seasonIds = item.seasonIds,
            colorId = item.colorId,
            materialId = item.materialId,
            labelIds = item.labels.map { it.id },
            storagePlace = item.storagePlace,
            comment = item.comment
        )
        val updated = clothingItemApi.updateItem(id, request)
        clothingItemDao.insert(updated.toEntity())
        updated.toDomain()
    }

    override suspend fun deleteItem(id: String): Result<Unit> = runCatching {
        clothingItemApi.deleteItem(id)
        clothingItemDao.deleteById(id)
    }

    override suspend fun getTemplateItems(): Result<List<TemplateItem>> = runCatching {
        clothingItemApi.getTemplateItems().map { it.toDomain() }
    }

    override suspend fun createItemsFromTemplates(templateIds: List<String>): Result<Unit> = runCatching {
        clothingItemApi.createItemsFromTemplates(CreateFromTemplatesRequestDto(templateIds))
    }

    override suspend fun getCompatibleItems(id: String, categoryGroupId: String?): Result<List<ClothingItem>> = runCatching {
        clothingItemApi.getCompatibleItems(id, categoryGroupId).map { it.toDomain() }
    }

    override suspend fun addCompatibility(itemId: String, compatibleItemId: String): Result<Unit> = runCatching {
        clothingItemApi.addCompatibility(itemId, mapOf("compatibleItemId" to compatibleItemId))
    }

    override suspend fun getItemOutfits(id: String): Result<List<Outfit>> = runCatching {
        clothingItemApi.getItemOutfits(id).map { it.toDomain() }
    }
}