package com.example.wardrobe_client.data.repository

import com.example.wardrobe_client.data.local.dao.ReferencesDao
import com.example.wardrobe_client.data.local.entity.ReferencesEntity
import com.example.wardrobe_client.data.mapper.toDomain
import com.example.wardrobe_client.data.remote.api.ReferenceApi
import com.example.wardrobe_client.domain.model.References
import com.example.wardrobe_client.domain.repository.ReferenceRepository
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.example.wardrobe_client.data.remote.dto.ReferencesDto
import javax.inject.Inject

class ReferenceRepositoryImpl @Inject constructor(
    private val referenceApi: ReferenceApi,
    private val referencesDao: ReferencesDao,
    private val json: Json
) : ReferenceRepository {

    override suspend fun getReferences(): Result<References> = runCatching {
        try {
            val dto = referenceApi.getReferences()
            referencesDao.insert(ReferencesEntity(json = json.encodeToString(dto)))
            dto.toDomain()
        } catch (e: Exception) {
            val cached = referencesDao.getReferences()
                ?: throw Exception("Нет данных. Проверьте подключение к сети.")
            json.decodeFromString<ReferencesDto>(cached.json).toDomain()
        }
    }
}