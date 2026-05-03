package com.example.wardrobe_client.data.remote.api

import com.example.wardrobe_client.data.remote.dto.AuthResponseDto
import com.example.wardrobe_client.data.remote.dto.LoginRequestDto
import com.example.wardrobe_client.data.remote.dto.RegisterRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @DELETE("auth/account")
    suspend fun deleteAccount()
}