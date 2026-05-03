package com.example.wardrobe_client.presentation.screens.outfits

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.OutfitItem
import com.example.wardrobe_client.domain.model.References
import com.example.wardrobe_client.domain.usecase.GetReferencesUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemsUseCase
import com.example.wardrobe_client.domain.usecase.outfit.CreateOutfitUseCase
import com.example.wardrobe_client.domain.usecase.outfit.GetOutfitUseCase
import com.example.wardrobe_client.domain.usecase.outfit.UpdateOutfitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CanvasItem(
    val clothingItem: ClothingItem,
    val x: Float = 0.5f,
    val y: Float = 0.5f,
    val scale: Float = 1f
)

data class OutfitEditorUiState(
    val canvasItems: List<CanvasItem> = emptyList(),
    val availableItems: List<ClothingItem> = emptyList(),
    val references: References? = null,
    val selectedStyleId: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OutfitEditorViewModel @Inject constructor(
    private val createOutfitUseCase: CreateOutfitUseCase,
    private val updateOutfitUseCase: UpdateOutfitUseCase,
    private val getOutfitUseCase: GetOutfitUseCase,
    private val getClothingItemsUseCase: GetClothingItemsUseCase,
    private val getReferencesUseCase: GetReferencesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val outfitId: String? = savedStateHandle["outfitId"]
    val isEditMode = outfitId != null

    private val _uiState = MutableStateFlow(OutfitEditorUiState())
    val uiState: StateFlow<OutfitEditorUiState> = _uiState.asStateFlow()

    init {
        loadReferences()
        loadAvailableItems()
        if (isEditMode) loadOutfit()
    }

    private fun loadReferences() {
        viewModelScope.launch {
            getReferencesUseCase()
                .onSuccess { references ->
                    _uiState.value = _uiState.value.copy(references = references)
                }
        }
    }

    private fun loadAvailableItems() {
        viewModelScope.launch {
            getClothingItemsUseCase()
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(availableItems = items)
                }
        }
    }

    private fun loadOutfit() {
        viewModelScope.launch {
            getOutfitUseCase(outfitId!!)
                .onSuccess { outfit ->
                    _uiState.value = _uiState.value.copy(selectedStyleId = outfit.styleId)
                }
        }
    }

    fun addItemToCanvas(item: ClothingItem) {
        val current = _uiState.value.canvasItems.toMutableList()
        current.add(CanvasItem(clothingItem = item))
        _uiState.value = _uiState.value.copy(canvasItems = current)
    }

    fun updateItemPosition(index: Int, x: Float, y: Float) {
        val current = _uiState.value.canvasItems.toMutableList()
        if (index < current.size) {
            current[index] = current[index].copy(x = x, y = y)
            _uiState.value = _uiState.value.copy(canvasItems = current)
        }
    }

    fun updateItemScale(index: Int, scale: Float) {
        val current = _uiState.value.canvasItems.toMutableList()
        if (index < current.size) {
            current[index] = current[index].copy(scale = scale)
            _uiState.value = _uiState.value.copy(canvasItems = current)
        }
    }

    fun removeItemFromCanvas(index: Int) {
        val current = _uiState.value.canvasItems.toMutableList()
        if (index < current.size) {
            current.removeAt(index)
            _uiState.value = _uiState.value.copy(canvasItems = current)
        }
    }

    fun onStyleChange(styleId: String) {
        _uiState.value = _uiState.value.copy(selectedStyleId = styleId)
    }

    fun save(coverUrl: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val items = _uiState.value.canvasItems.map {
                OutfitItem(
                    itemId = it.clothingItem.id,
                    x = it.x,
                    y = it.y,
                    scale = it.scale
                )
            }
            val result = if (isEditMode) {
                updateOutfitUseCase(outfitId!!, coverUrl, _uiState.value.selectedStyleId, items)
            } else {
                createOutfitUseCase(coverUrl, _uiState.value.selectedStyleId, items)
            }
            result
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка сохранения"
                    )
                }
        }
    }
}