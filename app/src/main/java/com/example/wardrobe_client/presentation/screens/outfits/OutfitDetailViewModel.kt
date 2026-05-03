package com.example.wardrobe_client.presentation.screens.outfits

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.usecase.outfit.DeleteOutfitUseCase
import com.example.wardrobe_client.domain.usecase.outfit.GetOutfitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OutfitDetailUiState(
    val outfit: Outfit? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OutfitDetailViewModel @Inject constructor(
    private val getOutfitUseCase: GetOutfitUseCase,
    private val deleteOutfitUseCase: DeleteOutfitUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val outfitId: String = checkNotNull(savedStateHandle["outfitId"])

    private val _uiState = MutableStateFlow(OutfitDetailUiState())
    val uiState: StateFlow<OutfitDetailUiState> = _uiState.asStateFlow()

    init {
        loadOutfit()
    }

    fun loadOutfit() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getOutfitUseCase(outfitId)
                .onSuccess { outfit ->
                    _uiState.value = _uiState.value.copy(isLoading = false, outfit = outfit)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
        }
    }

    fun deleteOutfit() {
        viewModelScope.launch {
            deleteOutfitUseCase(outfitId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isDeleted = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(error = error.message ?: "Ошибка удаления")
                }
        }
    }
}