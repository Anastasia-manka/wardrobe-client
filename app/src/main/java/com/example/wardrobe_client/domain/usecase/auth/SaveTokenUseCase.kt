package com.example.wardrobe_client.domain.usecase.auth

import com.example.wardrobe_client.domain.repository.AuthRepository
import javax.inject.Inject

class SaveTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(token: String) = authRepository.saveToken(token)
}
