package com.example.wardrobe_client.domain.usecase.outfit

import com.example.wardrobe_client.domain.repository.OutfitRepository
import javax.inject.Inject

class DeleteOutfitUseCase @Inject constructor(
    private val repository: OutfitRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteOutfit(id)
}