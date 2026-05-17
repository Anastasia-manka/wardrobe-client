package com.example.wardrobe_client.domain.usecase.search

import com.example.wardrobe_client.domain.repository.VisualSearchRepository
import com.example.wardrobe_client.domain.repository.VisualSearchResult
import java.io.File
import javax.inject.Inject

class VisualSearchUseCase @Inject constructor(
    private val repository: VisualSearchRepository
) {
    suspend operator fun invoke(imageFile: File): VisualSearchResult =
        repository.searchByImage(imageFile)
}