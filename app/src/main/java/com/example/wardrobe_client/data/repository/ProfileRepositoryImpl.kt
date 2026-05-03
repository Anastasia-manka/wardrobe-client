package com.example.wardrobe_client.data.repository

import com.example.wardrobe_client.data.remote.api.ProfileApi
import com.example.wardrobe_client.data.remote.dto.UpdateProfileRequestDto
import com.example.wardrobe_client.domain.model.User
import com.example.wardrobe_client.domain.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi
) : ProfileRepository {

    override suspend fun getProfile(): Result<User> = runCatching {
        val dto = profileApi.getProfile()
        User(
            id = dto.id,
            email = dto.email,
            name = dto.name,
            gender = dto.gender
        )
    }

    override suspend fun updateProfile(name: String, gender: String): Result<User> = runCatching {
        val dto = profileApi.updateProfile(UpdateProfileRequestDto(name, gender))
        User(
            id = dto.id,
            email = dto.email,
            name = dto.name,
            gender = dto.gender
        )
    }
}