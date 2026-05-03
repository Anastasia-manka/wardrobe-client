package com.example.wardrobe_client.domain.usecase.outfit

import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.repository.OutfitRepository
import javax.inject.Inject

class GetOutfitsUseCase @Inject constructor(
    private val repository: OutfitRepository
) {
    suspend operator fun invoke(): Result<List<Outfit>> = repository.getOutfits()
}