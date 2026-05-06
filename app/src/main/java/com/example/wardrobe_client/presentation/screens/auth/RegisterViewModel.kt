package com.example.wardrobe_client.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.usecase.auth.RegisterUseCase
import com.example.wardrobe_client.domain.usecase.auth.SaveTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


data class RegisterUiState(
    val email: String = "",
    val name: String = "",
    val gender: String = "",
    val password: String = "",
    val passwordConfirm: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name, error = null)
    }

    fun onGenderChange(gender: String) {
        _uiState.value = _uiState.value.copy(gender = gender, error = null)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }

    fun onPasswordConfirmChange(password: String) {
        _uiState.value = _uiState.value.copy(passwordConfirm = password, error = null)
    }
    fun register() {
        if (_uiState.value.password != _uiState.value.passwordConfirm) {
            _uiState.value = _uiState.value.copy(error = "Пароли не совпадают")
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            registerUseCase(
                email = _uiState.value.email,
                name = _uiState.value.name,
                gender = _uiState.value.gender,
                password = _uiState.value.password
            )
                .onSuccess { token ->
                    saveTokenUseCase(token)
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка регистрации"
                    )
                }
        }
    }
}