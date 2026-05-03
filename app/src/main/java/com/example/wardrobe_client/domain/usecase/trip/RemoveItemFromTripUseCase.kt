package com.example.wardrobe_client.domain.usecase.trip

import com.example.wardrobe_client.domain.repository.TripRepository
import javax.inject.Inject

class RemoveItemFromTripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(tripId: String, itemId: String): Result<Unit> =
        repository.removeItemFromTrip(tripId, itemId)
}