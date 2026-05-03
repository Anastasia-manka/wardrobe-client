package com.example.wardrobe_client.presentation.screens.outfits

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.usecase.outfit.GetOutfitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OutfitsUiState(
    val outfits: List<Outfit> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OutfitsViewModel @Inject constructor(
    private val getOutfitsUseCase: GetOutfitsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OutfitsUiState())
    val uiState: StateFlow<OutfitsUiState> = _uiState.asStateFlow()

    init {
        loadOutfits()
    }

    fun loadOutfits() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            getOutfitsUseCase()
                .onSuccess { outfits ->
                    _uiState.value = _uiState.value.copy(isLoading = false, outfits = outfits)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
        }
    }
}