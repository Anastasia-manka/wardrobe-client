package com.example.wardrobe_client.domain.usecase.clothing

import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.repository.ClothingItemRepository
import javax.inject.Inject

class GetItemOutfitsUseCase @Inject constructor(
    private val repository: ClothingItemRepository
) {
    suspend operator fun invoke(id: String): Result<List<Outfit>> = repository.getItemOutfits(id)
}