package com.example.wardrobe_client.presentation.screens.clothing

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.Label
import com.example.wardrobe_client.domain.model.References
import com.example.wardrobe_client.domain.usecase.GetReferencesUseCase
import com.example.wardrobe_client.domain.usecase.clothing.CreateClothingItemUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemUseCase
import com.example.wardrobe_client.domain.usecase.clothing.UpdateClothingItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditClothingItemUiState(
    val imageUrl: String = "",
    val categoryId: String = "",
    val seasonIds: List<String> = emptyList(),
    val colorId: String = "",
    val materialId: String = "",
    val labels: List<Label> = emptyList(),
    val storagePlace: String = "",
    val comment: String = "",
    val references: References? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddEditClothingItemViewModel @Inject constructor(
    private val createClothingItemUseCase: CreateClothingItemUseCase,
    private val updateClothingItemUseCase: UpdateClothingItemUseCase,
    private val getClothingItemUseCase: GetClothingItemUseCase,
    private val getReferencesUseCase: GetReferencesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String? = savedStateHandle["itemId"]
    val isEditMode = itemId != null

    private val _uiState = MutableStateFlow(AddEditClothingItemUiState())
    val uiState: StateFlow<AddEditClothingItemUiState> = _uiState.asStateFlow()

    init {
        loadReferences()
        if (isEditMode) loadItem()
    }

    private fun loadReferences() {
        viewModelScope.launch {
            getReferencesUseCase()
                .onSuccess { references ->
                    _uiState.value = _uiState.value.copy(references = references)
                }
        }
    }

    private fun loadItem() {
        viewModelScope.launch {
            getClothingItemUseCase(itemId!!)
                .onSuccess { item ->
                    _uiState.value = _uiState.value.copy(
                        imageUrl = item.imageUrl,
                        categoryId = item.categoryId,
                        seasonIds = item.seasonIds,
                        colorId = item.colorId,
                        materialId = item.materialId,
                        labels = item.labels,
                        storagePlace = item.storagePlace,
                        comment = item.comment
                    )
                }
        }
    }

    fun onImageUrlChange(url: String) {
        _uiState.value = _uiState.value.copy(imageUrl = url)
    }

    fun onCategoryChange(categoryId: String) {
        _uiState.value = _uiState.value.copy(categoryId = categoryId)
    }

    fun onSeasonToggle(seasonId: String) {
        val current = _uiState.value.seasonIds.toMutableList()
        if (current.contains(seasonId)) current.remove(seasonId) else current.add(seasonId)
        _uiState.value = _uiState.value.copy(seasonIds = current)
    }

    fun onColorChange(colorId: String) {
        _uiState.value = _uiState.value.copy(colorId = colorId)
    }

    fun onMaterialChange(materialId: String) {
        _uiState.value = _uiState.value.copy(materialId = materialId)
    }

    fun onLabelToggle(label: Label) {
        val current = _uiState.value.labels.toMutableList()
        if (current.any { it.id == label.id }) current.removeAll { it.id == label.id }
        else current.add(label)
        _uiState.value = _uiState.value.copy(labels = current)
    }

    fun onStoragePlaceChange(place: String) {
        _uiState.value = _uiState.value.copy(storagePlace = place)
    }

    fun onCommentChange(comment: String) {
        _uiState.value = _uiState.value.copy(comment = comment)
    }

    fun save() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val state = _uiState.value
            val item = ClothingItem(
                id = itemId ?: "",
                imageUrl = state.imageUrl,
                categoryId = state.categoryId,
                categoryName = "",
                categoryGroupName = "",
                seasonIds = state.seasonIds,
                seasonNames = emptyList(),
                colorId = state.colorId,
                colorName = "",
                materialId = state.materialId,
                materialName = "",
                labels = state.labels,
                storagePlace = state.storagePlace,
                comment = state.comment
            )
            val result = if (isEditMode) {
                updateClothingItemUseCase(itemId!!, item, state.imageUrl)
            } else {
                createClothingItemUseCase(item, state.imageUrl)
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