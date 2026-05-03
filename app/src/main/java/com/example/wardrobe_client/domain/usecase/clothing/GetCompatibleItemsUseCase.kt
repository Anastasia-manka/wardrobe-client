package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class GetCompatibleItemsUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(id: String, categoryGroupId: String? = null): Result<List<ClothingItem>> =
        repository.getCompatibleItems(id, categoryGroupId)
}