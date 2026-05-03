package com.example.wardrobe_client.presentation.screens.clothing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.References
import com.example.wardrobe_client.domain.usecase.GetReferencesUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClothingUiState(
    val items: List<ClothingItem> = emptyList(),
    val references: References? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategoryId: String? = null,
    val selectedSeasonId: String? = null,
    val selectedColorId: String? = null,
    val selectedMaterialId: String? = null,
    val selectedLabelId: String? = null
)

@HiltViewModel
class ClothingViewModel @Inject constructor(
    private val getClothingItemsUseCase: GetClothingItemsUseCase,
    private val getReferencesUseCase: GetReferencesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClothingUiState())
    val uiState: StateFlow<ClothingUiState> = _uiState.asStateFlow()

    init {
        loadReferences()
        loadItems()
    }

    private fun loadReferences() {
        viewModelScope.launch {
            getReferencesUseCase()
                .onSuccess { references ->
                    _uiState.value = _uiState.value.copy(references = references)
                }
        }
    }

    fun loadItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val state = _uiState.value
            getClothingItemsUseCase(
                categoryId = state.selectedCategoryId,
                seasonId = state.selectedSeasonId,
                colorId = state.selectedColorId,
                materialId = state.selectedMaterialId,
                labelId = state.selectedLabelId
            )
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(isLoading = false, items = items)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
        }
    }

    fun onCategoryFilter(categoryId: String?) {
        _uiState.value = _uiState.value.copy(selectedCategoryId = categoryId)
        loadItems()
    }

    fun onSeasonFilter(seasonId: String?) {
        _uiState.value = _uiState.value.copy(selectedSeasonId = seasonId)
        loadItems()
    }

    fun onColorFilter(colorId: String?) {
        _uiState.value = _uiState.value.copy(selectedColorId = colorId)
        loadItems()
    }

    fun onMaterialFilter(materialId: String?) {
        _uiState.value = _uiState.value.copy(selectedMaterialId = materialId)
        loadItems()
    }

    fun onLabelFilter(labelId: String?) {
        _uiState.value = _uiState.value.copy(selectedLabelId = labelId)
        loadItems()
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            selectedCategoryId = null,
            selectedSeasonId = null,
            selectedColorId = null,
            selectedMaterialId = null,
            selectedLabelId = null
        )
        loadItems()
    }
}