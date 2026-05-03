package com.example.wardrobe_client.domain.usecase

import com.example.wardrobe_client.domain.model.References
import com.example.wardrobe_client.domain.repository.ReferenceRepository
import javax.inject.Inject

class GetReferencesUseCase @Inject constructor(
    private val repository: ReferenceRepository
) {
    suspend operator fun invoke(): Result<References> = repository.getReferences()
}