package com.example.wardrobe_client.data.remote.api

import com.example.wardrobe_client.data.remote.dto.ReferencesDto
import retrofit2.http.GET

interface ReferenceApi {
    @GET("references")
    suspend fun getReferences(): ReferencesDto
}