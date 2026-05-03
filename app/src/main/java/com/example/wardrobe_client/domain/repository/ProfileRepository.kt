package com.example.wardrobe_client.domain.repository

import com.example.wardrobe_client.domain.model.User

interface ProfileRepository {
    suspend fun getProfile(): Result<User>
    suspend fun updateProfile(name: String, gender: String): Result<User>
}