package com.example.wardrobe_client.presentation.screens.clothing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.usecase.clothing.AddCompatibilityUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SwipeCompatibilityUiState(
    val currentItem: ClothingItem? = null,
    val queue: List<ClothingItem> = emptyList(),
    val selectedCategoryGroupId: String? = null,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)

@HiltViewModel
class SwipeCompatibilityViewModel @Inject constructor(
    private val getClothingItemsUseCase: GetClothingItemsUseCase,
    private val addCompatibilityUseCase: AddCompatibilityUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle["itemId"])

    private val _uiState = MutableStateFlow(SwipeCompatibilityUiState())
    val uiState: StateFlow<SwipeCompatibilityUiState> = _uiState.asStateFlow()

    init {
        loadItems()
    }

    fun loadItems(categoryGroupId: String? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, selectedCategoryGroupId = categoryGroupId)
            getClothingItemsUseCase(categoryId = categoryGroupId)
                .onSuccess { items ->
                    val filtered = items.filter { it.id != itemId }
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        queue = filtered,
                        currentItem = filtered.firstOrNull(),
                        isEmpty = filtered.isEmpty()
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false, isEmpty = true)
                }
        }
    }

    fun swipeRight() {
        val current = _uiState.value.currentItem ?: return
        viewModelScope.launch {
            addCompatibilityUseCase(itemId, current.id)
        }
        nextItem()
    }

    fun swipeLeft() {
        nextItem()
    }

    private fun nextItem() {
        val queue = _uiState.value.queue.toMutableList()
        if (queue.isNotEmpty()) queue.removeAt(0)
        _uiState.value = _uiState.value.copy(
            queue = queue,
            currentItem = queue.firstOrNull(),
            isEmpty = queue.isEmpty()
        )
    }
}