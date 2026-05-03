package com.example.wardrobe_client.presentation.screens.clothing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.Outfit
import com.example.wardrobe_client.domain.usecase.clothing.DeleteClothingItemUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetCompatibleItemsUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetItemOutfitsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClothingItemDetailUiState(
    val item: ClothingItem? = null,
    val outfits: List<Outfit> = emptyList(),
    val compatibleItems: List<ClothingItem> = emptyList(),
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null,
    val deleteError: String? = null
)

@HiltViewModel
class ClothingItemDetailViewModel @Inject constructor(
    private val getClothingItemUseCase: GetClothingItemUseCase,
    private val deleteClothingItemUseCase: DeleteClothingItemUseCase,
    private val getItemOutfitsUseCase: GetItemOutfitsUseCase,
    private val getCompatibleItemsUseCase: GetCompatibleItemsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle["itemId"])

    private val _uiState = MutableStateFlow(ClothingItemDetailUiState())
    val uiState: StateFlow<ClothingItemDetailUiState> = _uiState.asStateFlow()

    init {
        loadItem()
        loadOutfits()
        loadCompatibleItems()
    }

    fun loadItem() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getClothingItemUseCase(itemId)
                .onSuccess { item ->
                    _uiState.value = _uiState.value.copy(isLoading = false, item = item)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
        }
    }

    private fun loadOutfits() {
        viewModelScope.launch {
            getItemOutfitsUseCase(itemId)
                .onSuccess { outfits ->
                    _uiState.value = _uiState.value.copy(outfits = outfits)
                }
        }
    }

    fun loadCompatibleItems(categoryGroupId: String? = null) {
        viewModelScope.launch {
            getCompatibleItemsUseCase(itemId, categoryGroupId)
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(compatibleItems = items)
                }
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            deleteClothingItemUseCase(itemId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isDeleted = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        deleteError = error.message ?: "Невозможно удалить вещь"
                    )
                }
        }
    }

    fun clearDeleteError() {
        _uiState.value = _uiState.value.copy(deleteError = null)
    }
}