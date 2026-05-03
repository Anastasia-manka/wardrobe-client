package com.example.wardrobe_client.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val email: String,
    val name: String,
    val gender: String,
    val password: String
)

@Serializable
data class LoginRequestDto(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponseDto(
    val token: String
)