package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class CreateItemsFromTemplatesUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(templateIds: List<String>): Result<Unit> =
        repository.createItemsFromTemplates(templateIds)
}