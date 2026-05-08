package com.example.wardrobe_client.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.User
import com.example.wardrobe_client.domain.usecase.auth.DeleteAccountUseCase
import com.example.wardrobe_client.domain.usecase.auth.LogoutUseCase
import com.example.wardrobe_client.domain.usecase.profile.GetProfileUseCase
import com.example.wardrobe_client.domain.usecase.profile.UpdateProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isLoggedOut: Boolean = false,
    val isAccountDeleted: Boolean = false,
    val showLanguageDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getProfileUseCase()
                .onSuccess { user ->
                    _uiState.value = _uiState.value.copy(isLoading = false, user = user)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiState.value = _uiState.value.copy(isLoggedOut = true)
        }
    }

    fun deleteAccount() {
        viewModelScope.launch {
            deleteAccountUseCase()
                .onSuccess {
                    logoutUseCase()
                    _uiState.value = _uiState.value.copy(isAccountDeleted = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        error = error.message ?: "Ошибка удаления"
                    )
                }
        }
    }

    fun showLanguageDialog() {
        _uiState.value = _uiState.value.copy(showLanguageDialog = true)
    }

    fun hideLanguageDialog() {
        _uiState.value = _uiState.value.copy(showLanguageDialog = false)
    }

    fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }
}