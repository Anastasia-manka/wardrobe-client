package com.example.wardrobe_client.data.remote.api

import com.example.wardrobe_client.data.remote.dto.ClothingItemDto
import com.example.wardrobe_client.data.remote.dto.CreateClothingItemRequestDto
import com.example.wardrobe_client.data.remote.dto.CreateFromTemplatesRequestDto
import com.example.wardrobe_client.data.remote.dto.OutfitDto
import com.example.wardrobe_client.data.remote.dto.TemplateItemDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ClothingItemApi {
    @GET("items")
    suspend fun getItems(
        @Query("categoryId") categoryId: String? = null,
        @Query("seasonId") seasonId: String? = null,
        @Query("colorId") colorId: String? = null,
        @Query("materialId") materialId: String? = null,
        @Query("labelId") labelId: String? = null
    ): List<ClothingItemDto>

    @GET("items/{id}")
    suspend fun getItem(@Path("id") id: String): ClothingItemDto

    @POST("items")
    suspend fun createItem(@Body request: CreateClothingItemRequestDto): ClothingItemDto

    @PUT("items/{id}")
    suspend fun updateItem(
        @Path("id") id: String,
        @Body request: CreateClothingItemRequestDto
    ): ClothingItemDto

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: String)

    @GET("items/templates")
    suspend fun getTemplateItems(): List<TemplateItemDto>

    @POST("items/from-templates")
    suspend fun createItemsFromTemplates(@Body request: CreateFromTemplatesRequestDto)

    @GET("items/{id}/compatible")
    suspend fun getCompatibleItems(
        @Path("id") id: String,
        @Query("categoryGroupId") categoryGroupId: String? = null
    ): List<ClothingItemDto>

    @POST("items/{id}/compatibility")
    suspend fun addCompatibility(
        @Path("id") id: String,
        @Body request: Map<String, String>
    )
    @DELETE("items/{id}/compatibility/{compatibleItemId}")
    suspend fun deleteCompatibility(
        @Path("id") itemId: String,
        @Path("compatibleItemId") compatibleItemId: String
    )

    @GET("items/{id}/outfits")
    suspend fun getItemOutfits(@Path("id") id: String): List<OutfitDto>
}