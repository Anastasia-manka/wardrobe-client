package com.example.wardrobe_client.domain.usecase.auth

import com.example.wardrobe_client.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        email: String,
        name: String,
        gender: String,
        password: String
    ): Result<String> = authRepository.register(email, name, gender, password)
}