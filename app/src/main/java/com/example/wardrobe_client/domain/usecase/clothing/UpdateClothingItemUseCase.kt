package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class UpdateClothingItemUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(id: String, item: ClothingItem, imageUrl: String?): Result<ClothingItem> =
        repository.updateItem(id, item, imageUrl)
}