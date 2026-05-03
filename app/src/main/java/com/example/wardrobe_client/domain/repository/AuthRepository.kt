package com.example.wardrobe_client.domain.repository

import com.example.wardrobe_client.domain.model.User

interface AuthRepository {
    suspend fun register(email: String, name: String, gender: String, password: String): Result<String>
    suspend fun login(email: String, password: String): Result<String>
    suspend fun deleteAccount(): Result<Unit>
    suspend fun saveToken(token: String)
    suspend fun getToken(): String?
    suspend fun clearToken()
}