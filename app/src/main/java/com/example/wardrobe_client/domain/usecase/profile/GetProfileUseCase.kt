package com.example.wardrobe_client.domain.usecase.profile

import com.example.wardrobe_client.domain.model.User
import com.example.wardrobe_client.domain.repository.ProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<User> = repository.getProfile()
}