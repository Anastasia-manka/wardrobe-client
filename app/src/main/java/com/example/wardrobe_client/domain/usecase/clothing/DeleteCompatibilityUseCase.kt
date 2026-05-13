package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class DeleteCompatibilityUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(itemId: String, compatibleItemId: String): Result<Unit> =
        repository.deleteCompatibility(itemId, compatibleItemId)
}