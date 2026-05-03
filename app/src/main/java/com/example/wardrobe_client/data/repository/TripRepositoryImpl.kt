package com.example.wardrobe_client.data.repository

import com.example.wardrobe_client.data.local.dao.TripDao
import com.example.wardrobe_client.data.mapper.toDomain
import com.example.wardrobe_client.data.mapper.toEntity
import com.example.wardrobe_client.data.remote.api.TripApi
import com.example.wardrobe_client.data.remote.dto.AddTripItemRequestDto
import com.example.wardrobe_client.data.remote.dto.CreateTripRequestDto
import com.example.wardrobe_client.data.remote.dto.UpdatePackingStatusRequestDto
import com.example.wardrobe_client.domain.model.Trip
import com.example.wardrobe_client.domain.repository.TripRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val tripApi: TripApi,
    private val tripDao: TripDao
) : TripRepository {

    override suspend fun getTrips(): Result<List<Trip>> = runCatching {
        try {
            val trips = tripApi.getTrips()
            tripDao.insertAll(trips.map { it.toEntity() })
            trips.map { it.toDomain() }
        } catch (e: Exception) {
            tripDao.getAllTrips().first().map { it.toDomain() }
        }
    }

    override suspend fun getTrip(id: String): Result<Trip> = runCatching {
        try {
            val trip = tripApi.getTrip(id)
            tripDao.insert(trip.toEntity())
            trip.toDomain()
        } catch (e: Exception) {
            tripDao.getTripById(id)?.toDomain()
                ?: throw Exception("Поездка не найдена")
        }
    }

    override suspend fun createTrip(trip: Trip): Result<Trip> = runCatching {
        val request = CreateTripRequestDto(
            name = trip.name,
            tripDate = trip.tripDate,
            tripTypeId = trip.tripTypeId,
            climateId = trip.climateId,
            luggageTypeId = trip.luggageTypeId,
            activityIds = trip.activities.map { it.id }
        )
        val created = tripApi.createTrip(request)
        tripDao.insert(created.toEntity())
        created.toDomain()
    }

    override suspend fun updateTrip(id: String, trip: Trip): Result<Trip> = runCatching {
        val request = CreateTripRequestDto(
            name = trip.name,
            tripDate = trip.tripDate,
            tripTypeId = trip.tripTypeId,
            climateId = trip.climateId,
            luggageTypeId = trip.luggageTypeId,
            activityIds = trip.activities.map { it.id }
        )
        val updated = tripApi.updateTrip(id, request)
        tripDao.insert(updated.toEntity())
        updated.toDomain()
    }

    override suspend fun deleteTrip(id: String): Result<Unit> = runCatching {
        tripApi.deleteTrip(id)
        tripDao.deleteById(id)
    }

    override suspend fun addItemToTrip(tripId: String, itemId: String): Result<Unit> = runCatching {
        tripApi.addItemToTrip(tripId, AddTripItemRequestDto(itemId))
    }

    override suspend fun updatePackingStatus(
        tripId: String,
        itemId: String,
        isPacked: Boolean
    ): Result<Unit> = runCatching {
        tripApi.updatePackingStatus(tripId, itemId, UpdatePackingStatusRequestDto(isPacked))
    }

    override suspend fun removeItemFromTrip(tripId: String, itemId: String): Result<Unit> = runCatching {
        tripApi.removeItemFromTrip(tripId, itemId)
    }
}