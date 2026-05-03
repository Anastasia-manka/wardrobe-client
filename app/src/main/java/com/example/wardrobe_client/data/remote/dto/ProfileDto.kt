package com.example.wardrobe_client.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class ProfileDto(
    val id: String,
    val email: String,
    val name: String,
    val gender: String
)

@Serializable
data class UpdateProfileRequestDto(
    val name: String,
    val gender: String
)