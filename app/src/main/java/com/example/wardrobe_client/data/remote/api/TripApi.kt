package com.example.wardrobe_client.data.remote.api

import com.example.wardrobe_client.data.remote.dto.AddTripItemRequestDto
import com.example.wardrobe_client.data.remote.dto.CreateTripRequestDto
import com.example.wardrobe_client.data.remote.dto.TripDto
import com.example.wardrobe_client.data.remote.dto.UpdatePackingStatusRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TripApi {
    @GET("trips")
    suspend fun getTrips(): List<TripDto>

    @GET("trips/{id}")
    suspend fun getTrip(@Path("id") id: String): TripDto

    @POST("trips")
    suspend fun createTrip(@Body request: CreateTripRequestDto): TripDto

    @PUT("trips/{id}")
    suspend fun updateTrip(
        @Path("id") id: String,
        @Body request: CreateTripRequestDto
    ): TripDto

    @DELETE("trips/{id}")
    suspend fun deleteTrip(@Path("id") id: String)

    @POST("trips/{id}/items")
    suspend fun addItemToTrip(
        @Path("id") id: String,
        @Body request: AddTripItemRequestDto
    )

    @PATCH("trips/{id}/items/{itemId}")
    suspend fun updatePackingStatus(
        @Path("id") id: String,
        @Path("itemId") itemId: String,
        @Body request: UpdatePackingStatusRequestDto
    )

    @DELETE("trips/{id}/items/{itemId}")
    suspend fun removeItemFromTrip(
        @Path("id") id: String,
        @Path("itemId") itemId: String
    )
}