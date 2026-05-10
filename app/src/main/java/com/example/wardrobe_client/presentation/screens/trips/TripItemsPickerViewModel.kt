package com.example.wardrobe_client.presentation.screens.trips

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemsUseCase
import com.example.wardrobe_client.domain.usecase.trip.AddItemToTripUseCase
import com.example.wardrobe_client.domain.usecase.trip.RemoveItemFromTripUseCase
import com.example.wardrobe_client.domain.usecase.trip.GetTripUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TripItemsPickerUiState(
    val items: List<ClothingItem> = emptyList(),
    val addedItemIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TripItemsPickerViewModel @Inject constructor(
    private val getClothingItemsUseCase: GetClothingItemsUseCase,
    private val getTripUseCase: GetTripUseCase,
    private val addItemToTripUseCase: AddItemToTripUseCase,
    private val removeItemFromTripUseCase: RemoveItemFromTripUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tripId: String = checkNotNull(savedStateHandle["tripId"])

    private val _uiState = MutableStateFlow(TripItemsPickerUiState())
    val uiState: StateFlow<TripItemsPickerUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getClothingItemsUseCase()
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(items = items, isLoading = false)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
            getTripUseCase(tripId)
                .onSuccess { trip ->
                    _uiState.value = _uiState.value.copy(
                        addedItemIds = trip.items.map { it.itemId }.toSet()
                    )
                }
        }
    }

    fun onSearchChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun toggleItem(itemId: String) {
        val current = _uiState.value.addedItemIds
        viewModelScope.launch {
            if (itemId in current) {
                removeItemFromTripUseCase(tripId, itemId)
                    .onSuccess {
                        _uiState.value = _uiState.value.copy(
                            addedItemIds = current - itemId
                        )
                    }
            } else {
                addItemToTripUseCase(tripId, itemId)
                    .onSuccess {
                        _uiState.value = _uiState.value.copy(
                            addedItemIds = current + itemId
                        )
                    }
            }
        }
    }
    fun getTripId(): String = tripId

    fun filteredItems(): List<ClothingItem> {
        val query = _uiState.value.searchQuery.trim().lowercase()
        if (query.isBlank()) return _uiState.value.items
        return _uiState.value.items.filter {
            it.categoryName.lowercase().contains(query) ||
                    it.colorName.lowercase().contains(query) ||
                    it.categoryGroupName.lowercase().contains(query)
        }
    }
}