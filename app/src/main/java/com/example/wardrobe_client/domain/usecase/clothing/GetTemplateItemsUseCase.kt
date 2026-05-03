package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.model.TemplateItem
import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class GetTemplateItemsUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(): Result<List<TemplateItem>> = repository.getTemplateItems()
}