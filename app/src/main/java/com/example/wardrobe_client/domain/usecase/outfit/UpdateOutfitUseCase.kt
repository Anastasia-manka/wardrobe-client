package com.example.wardrobe_client.domain.usecase.outfit

import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.model.OutfitItem
import com.example.wardrobe_client.domain.repository.OutfitRepository
import javax.inject.Inject

class UpdateOutfitUseCase @Inject constructor(
    private val repository: OutfitRepository
) {
    suspend operator fun invoke(
        id: String,
        coverUrl: String,
        styleId: String,
        items: List<OutfitItem>
    ): Result<Outfit> = repository.updateOutfit(id, coverUrl, styleId, items)
}