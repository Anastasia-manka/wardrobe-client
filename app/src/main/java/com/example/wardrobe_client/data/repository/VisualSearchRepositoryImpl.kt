package com.example.wardrobe_client.data.repository

import com.example.wardrobe_client.data.remote.api.VisualSearchApi
import com.example.wardrobe_client.data.mapper.toClothingItem
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.repository.VisualSearchRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class VisualSearchRepositoryImpl @Inject constructor(
    private val api: VisualSearchApi
) : VisualSearchRepository {

    override suspend fun searchByImage(imageFile: File): List<ClothingItem> {
        val requestBody = imageFile.asRequestBody("image/*".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        return api.searchByImage(part).map { it.toClothingItem() }
    }
}