package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class CreateClothingItemUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(item: ClothingItem, imageUrl: String): Result<ClothingItem> =
        repository.createItem(item, imageUrl)
}