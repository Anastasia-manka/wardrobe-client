package com.example.wardrobe_client.domain.usecase.auth

import com.example.wardrobe_client.domain.repository.AuthRepository
import javax.inject.Inject

class GetTokenUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): String? = authRepository.getToken()
}