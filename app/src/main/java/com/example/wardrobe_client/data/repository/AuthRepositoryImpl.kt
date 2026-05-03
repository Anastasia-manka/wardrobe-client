package com.example.wardrobe_client.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.wardrobe_client.data.remote.api.AuthApi
import com.example.wardrobe_client.data.remote.dto.LoginRequestDto
import com.example.wardrobe_client.data.remote.dto.RegisterRequestDto
import com.example.wardrobe_client.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val dataStore: DataStore<Preferences>
) : AuthRepository {

    companion object {
        private val TOKEN_KEY = stringPreferencesKey("jwt_token")
    }

    override suspend fun register(
        email: String,
        name: String,
        gender: String,
        password: String
    ): Result<String> = runCatching {
        val response = authApi.register(RegisterRequestDto(email, name, gender, password))
        response.token
    }

    override suspend fun login(email: String, password: String): Result<String> = runCatching {
        val response = authApi.login(LoginRequestDto(email, password))
        response.token
    }

    override suspend fun deleteAccount(): Result<Unit> = runCatching {
        authApi.deleteAccount()
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN_KEY] = token }
    }

    override suspend fun getToken(): String? {
        return dataStore.data.map { it[TOKEN_KEY] }.first()
    }

    override suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }
}