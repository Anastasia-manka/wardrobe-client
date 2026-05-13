package com.example.wardrobe_client.presentation.screens.clothing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.usecase.clothing.AddCompatibilityUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemsUseCase
import com.example.wardrobe_client.domain.usecase.GetReferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SwipeCompatibilityUiState(
    val sourceItem: ClothingItem? = null,
    val currentItem: ClothingItem? = null,
    val queue: List<ClothingItem> = emptyList(),
    val allItems: List<ClothingItem> = emptyList(),
    val selectedCategoryGroupName: String? = null,
    val isLoading: Boolean = false,
    val isEmpty: Boolean = false
)

@HiltViewModel
class SwipeCompatibilityViewModel @Inject constructor(
    private val getClothingItemUseCase: GetClothingItemUseCase,
    private val getClothingItemsUseCase: GetClothingItemsUseCase,
    private val addCompatibilityUseCase: AddCompatibilityUseCase,
    private val getReferencesUseCase: GetReferencesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle["itemId"])

    private val _uiState = MutableStateFlow(SwipeCompatibilityUiState())
    val uiState: StateFlow<SwipeCompatibilityUiState> = _uiState.asStateFlow()

    private var categoryGroupMap: Map<String, String> = emptyMap()

    init {
        loadReferencesAndItems()
        loadSourceItem()
    }

    private fun loadReferencesAndItems() {
        viewModelScope.launch {
            getReferencesUseCase().onSuccess { references ->
                categoryGroupMap = buildMap {
                    references.categoryGroups.forEach { group ->
                        group.categories.forEach { category ->
                            put(category.id, group.name)
                        }
                    }
                }
            }
            loadItems()
        }
    }

    private fun loadSourceItem() {
        viewModelScope.launch {
            getClothingItemUseCase(itemId).onSuccess { item ->
                _uiState.value = _uiState.value.copy(sourceItem = item)
            }
        }
    }

    fun loadItems() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getClothingItemsUseCase().onSuccess { items ->
                val filtered = items
                    .filter { it.id != itemId }
                    .map { item ->
                        val groupName = categoryGroupMap[item.categoryId] ?: item.categoryGroupName
                        item.copy(categoryGroupName = groupName)
                    }
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    allItems = filtered,
                    queue = filtered,
                    currentItem = filtered.firstOrNull(),
                    isEmpty = filtered.isEmpty()
                )
            }.onFailure {
                _uiState.value = _uiState.value.copy(isLoading = false, isEmpty = true)
            }
        }
    }

    fun filterByGroup(groupName: String?) {
        val filtered = if (groupName == null) {
            _uiState.value.allItems
        } else {
            _uiState.value.allItems.filter { it.categoryGroupName == groupName }
        }
        _uiState.value = _uiState.value.copy(
            selectedCategoryGroupName = groupName,
            queue = filtered,
            currentItem = filtered.firstOrNull(),
            isEmpty = filtered.isEmpty()
        )
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