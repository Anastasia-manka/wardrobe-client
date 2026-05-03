package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class DeleteClothingItemUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteItem(id)
}