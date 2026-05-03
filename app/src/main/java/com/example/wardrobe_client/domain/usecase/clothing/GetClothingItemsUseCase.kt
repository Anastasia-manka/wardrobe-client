package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class GetClothingItemsUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(
        categoryId: String? = null,
        seasonId: String? = null,
        colorId: String? = null,
        materialId: String? = null,
        labelId: String? = null
    ): Result<List<ClothingItem>> = repository.getItems(categoryId, seasonId, colorId, materialId, labelId)
}