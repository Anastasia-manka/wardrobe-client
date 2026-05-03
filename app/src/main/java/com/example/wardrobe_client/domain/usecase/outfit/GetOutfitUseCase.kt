package com.example.wardrobe_client.domain.usecase.outfit

import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.repository.OutfitRepository
import javax.inject.Inject

class GetOutfitUseCase @Inject constructor(
    private val repository: OutfitRepository
) {
    suspend operator fun invoke(id: String): Result<Outfit> = repository.getOutfit(id)
}