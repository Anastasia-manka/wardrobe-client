package com.example.wardrobe_client.domain.usecase.trip

import com.example.wardrobe_client.domain.model.Trip
import com.example.wardrobe_client.domain.repository.TripRepository
import javax.inject.Inject

class UpdateTripUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(id: String, trip: Trip): Result<Trip> =
        repository.updateTrip(id, trip)
}