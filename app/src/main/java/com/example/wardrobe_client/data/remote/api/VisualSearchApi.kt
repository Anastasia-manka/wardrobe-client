package com.example.wardrobe_client.data.remote.api

import com.example.wardrobe_client.data.remote.dto.VisualSearchResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface VisualSearchApi {
    @Multipart
    @POST("search/visual")
    suspend fun searchByImage(
        @Part image: MultipartBody.Part
    ): VisualSearchResponseDto
}