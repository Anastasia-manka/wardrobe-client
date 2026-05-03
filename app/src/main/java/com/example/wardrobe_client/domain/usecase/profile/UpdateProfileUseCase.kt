package com.example.wardrobe_client.domain.usecase.profile

import com.example.wardrobe_client.domain.model.User
import com.example.wardrobe_client.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(name: String, gender: String): Result<User> =
        repository.updateProfile(name, gender)
}