package com.example.wardrobe_client.domain.repository

import com.example.wardrobe_client.domain.model.Trip

interface TripRepository {
    suspend fun getTrips(): Result<List<Trip>>
    suspend fun getTrip(id: String): Result<Trip>
    suspend fun createTrip(trip: Trip): Result<Trip>
    suspend fun updateTrip(id: String, trip: Trip): Result<Trip>
    suspend fun deleteTrip(id: String): Result<Unit>
    suspend fun addItemToTrip(tripId: String, itemId: String): Result<Unit>
    suspend fun updatePackingStatus(tripId: String, itemId: String, isPacked: Boolean): Result<Unit>
    suspend fun removeItemFromTrip(tripId: String, itemId: String): Result<Unit>
}