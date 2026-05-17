package com.example.wardrobe_client.data.repository

import com.example.wardrobe_client.data.remote.api.VisualSearchApi
import com.example.wardrobe_client.data.mapper.toClothingItem
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.repository.VisualSearchGroup
import com.example.wardrobe_client.domain.repository.VisualSearchRepository
import com.example.wardrobe_client.domain.repository.VisualSearchResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class VisualSearchRepositoryImpl @Inject constructor(
    private val api: VisualSearchApi
) : VisualSearchRepository {

    override suspend fun searchByImage(imageFile: File): VisualSearchResult {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        val response = api.searchByImage(part)
        return VisualSearchResult(
            grouped = response.grouped,
            items = response.items.map { it.toClothingItem() },
            groups = response.groups.map { group ->
                VisualSearchGroup(
                    categoryGroup = group.categoryGroup,
                    confidence = group.confidence,
                    items = group.items.map { it.toClothingItem() }
                )
            }
        )
    }
}