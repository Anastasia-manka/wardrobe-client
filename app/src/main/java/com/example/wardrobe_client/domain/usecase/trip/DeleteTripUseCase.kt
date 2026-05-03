package com.example.wardrobe_client.domain.usecase.trip

import com.example.wardrobe_client.domain.repository.TripRepository
import javax.inject.Inject

class DeleteTripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = repository.deleteTrip(id)
}