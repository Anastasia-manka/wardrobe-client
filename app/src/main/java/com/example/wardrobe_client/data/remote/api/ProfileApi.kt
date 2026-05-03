package com.example.wardrobe_client.data.remote.api

import com.example.wardrobe_client.data.remote.dto.ProfileDto
import com.example.wardrobe_client.data.remote.dto.UpdateProfileRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface ProfileApi {
    @GET("profile")
    suspend fun getProfile(): ProfileDto

    @PUT("profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequestDto): ProfileDto
}