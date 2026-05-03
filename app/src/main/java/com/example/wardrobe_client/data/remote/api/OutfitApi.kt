package com.example.wardrobe_client.data.remote.api

import com.example.wardrobe_client.data.remote.dto.CreateOutfitRequestDto
import com.example.wardrobe_client.data.remote.dto.OutfitDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OutfitApi {
    @GET("outfits")
    suspend fun getOutfits(): List<OutfitDto>

    @GET("outfits/{id}")
    suspend fun getOutfit(@Path("id") id: String): OutfitDto

    @POST("outfits")
    suspend fun createOutfit(@Body request: CreateOutfitRequestDto): OutfitDto

    @PUT("outfits/{id}")
    suspend fun updateOutfit(
        @Path("id") id: String,
        @Body request: CreateOutfitRequestDto
    ): OutfitDto

    @DELETE("outfits/{id}")
    suspend fun deleteOutfit(@Path("id") id: String)
}