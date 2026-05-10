package com.example.wardrobe_client.data.repository

import com.example.wardrobe_client.data.local.dao.OutfitDao
import com.example.wardrobe_client.data.mapper.toDomain
import com.example.wardrobe_client.data.mapper.toEntity
import com.example.wardrobe_client.data.remote.api.OutfitApi
import com.example.wardrobe_client.data.remote.dto.CreateOutfitRequestDto
import com.example.wardrobe_client.data.remote.dto.OutfitItemDto
import com.example.wardrobe_client.data.remote.dto.OutfitItemRequestDto
import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.model.OutfitItem
import com.example.wardrobe_client.domain.repository.OutfitRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OutfitRepositoryImpl @Inject constructor(
    private val outfitApi: OutfitApi,
    private val outfitDao: OutfitDao
) : OutfitRepository {

    override suspend fun getOutfits(): Result<List<Outfit>> = runCatching {
        try {
            val outfits = outfitApi.getOutfits()
            outfitDao.insertAll(outfits.map { it.toEntity() })
            outfits.map { it.toDomain() }
        } catch (e: Exception) {
            outfitDao.getAllOutfits().first().map { it.toDomain() }
        }
    }

    override suspend fun getOutfit(id: String): Result<Outfit> = runCatching {
        try {
            val outfit = outfitApi.getOutfit(id)
            outfitDao.insert(outfit.toEntity())
            outfit.toDomain()
        } catch (e: Exception) {
            outfitDao.getOutfitById(id)?.toDomain()
                ?: throw Exception("Наряд не найден")
        }
    }

    override suspend fun createOutfit(
        coverUrl: String,
        styleId: String,
        items: List<OutfitItem>
    ): Result<Outfit> = runCatching {
        val request = CreateOutfitRequestDto(
            coverUrl = coverUrl,
            styleId = styleId,
            items = items.map {
                OutfitItemRequestDto(
                    itemId = it.itemId,
                    position = """{"x":${it.x},"y":${it.y},"scale":${it.scale}}"""
                )
            }
        )
        val created = outfitApi.createOutfit(request)
        outfitDao.insert(created.toEntity())
        created.toDomain()
    }

    override suspend fun updateOutfit(
        id: String,
        coverUrl: String,
        styleId: String,
        items: List<OutfitItem>
    ): Result<Outfit> = runCatching {
        val request = CreateOutfitRequestDto(
            coverUrl = coverUrl,
            styleId = styleId,
            items = items.map {
                OutfitItemRequestDto(
                    itemId = it.itemId,
                    position = """{"x":${it.x},"y":${it.y},"scale":${it.scale}}"""
                )
            }
        )
        val updated = outfitApi.updateOutfit(id, request)
        outfitDao.insert(updated.toEntity())
        updated.toDomain()
    }

    override suspend fun deleteOutfit(id: String): Result<Unit> = runCatching {
        outfitApi.deleteOutfit(id)
        outfitDao.deleteById(id)
    }
}