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
import com.example.wardrobe_client.domain.model.References
import com.example.wardrobe_client.domain.usecase.GetReferencesUseCase
import com.example.wardrobe_client.domain.usecase.clothing.UpdateClothingItemUseCase
import com.example.wardrobe_client.domain.model.Label

data class ClothingItemDetailUiState(
    val item: ClothingItem? = null,
    val outfits: List<Outfit> = emptyList(),
    val compatibleItems: List<ClothingItem> = emptyList(),
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null,
    val deleteError: String? = null,
    val isEditing: Boolean = false,
    val hasChanges: Boolean = false,
    val editedCategoryId: String = "",
    val editedSeasonIds: List<String> = emptyList(),
    val editedColorId: String = "",
    val editedMaterialId: String = "",
    val editedLabelIds: List<String> = emptyList(),
    val editedStoragePlace: String = "",
    val editedComment: String = "",
    val isSaving: Boolean = false,
    val references: References? = null
)

@HiltViewModel
class ClothingItemDetailViewModel @Inject constructor(
    private val getClothingItemUseCase: GetClothingItemUseCase,
    private val deleteClothingItemUseCase: DeleteClothingItemUseCase,
    private val updateClothingItemUseCase: UpdateClothingItemUseCase,
    private val getItemOutfitsUseCase: GetItemOutfitsUseCase,
    private val getCompatibleItemsUseCase: GetCompatibleItemsUseCase,
    private val getReferencesUseCase: GetReferencesUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String = checkNotNull(savedStateHandle["itemId"])

    private val _uiState = MutableStateFlow(ClothingItemDetailUiState())
    val uiState: StateFlow<ClothingItemDetailUiState> = _uiState.asStateFlow()

    init {
        loadItem()
        loadOutfits()
        loadCompatibleItems()
        loadReferences()
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
    fun startEditing() {
        val item = _uiState.value.item ?: return
        _uiState.value = _uiState.value.copy(
            isEditing = true,
            editedCategoryId = item.categoryId,
            editedSeasonIds = item.seasonIds,
            editedColorId = item.colorId,
            editedMaterialId = item.materialId,
            editedLabelIds = item.labels.map { it.id },
            editedStoragePlace = item.storagePlace,
            editedComment = item.comment
        )
    }

    fun onCategoryChange(categoryId: String) {
        _uiState.value = _uiState.value.copy(
            editedCategoryId = categoryId,
            hasChanges = true
        )
    }

    fun onSeasonToggle(seasonId: String) {
        val current = _uiState.value.editedSeasonIds.toMutableList()
        if (current.contains(seasonId)) current.remove(seasonId) else current.add(seasonId)
        _uiState.value = _uiState.value.copy(
            editedSeasonIds = current,
            hasChanges = true
        )
    }

    fun onColorChange(colorId: String) {
        _uiState.value = _uiState.value.copy(
            editedColorId = colorId,
            hasChanges = true
        )
    }

    fun onMaterialChange(materialId: String) {
        _uiState.value = _uiState.value.copy(
            editedMaterialId = materialId,
            hasChanges = true
        )
    }

    fun onLabelToggle(labelId: String) {
        val current = _uiState.value.editedLabelIds.toMutableList()
        if (current.contains(labelId)) current.remove(labelId) else current.add(labelId)
        _uiState.value = _uiState.value.copy(
            editedLabelIds = current,
            hasChanges = true
        )
    }

    fun onStoragePlaceChange(place: String) {
        _uiState.value = _uiState.value.copy(
            editedStoragePlace = place,
            hasChanges = true
        )
    }

    fun onCommentChange(comment: String) {
        _uiState.value = _uiState.value.copy(
            editedComment = comment,
            hasChanges = true
        )
    }
    fun updateLabels(labelIds: List<String>) {
        _uiState.value = _uiState.value.copy(
            editedLabelIds = labelIds,
            hasChanges = true
        )
    }
    fun onSeasonSelect(seasonId: String) {
        _uiState.value = _uiState.value.copy(
            editedSeasonIds = listOf(seasonId),
            hasChanges = true
        )
    }
    private fun loadReferences() {
        viewModelScope.launch {
            getReferencesUseCase()
                .onSuccess { references ->
                    _uiState.value = _uiState.value.copy(references = references)
                }
        }
    }
    fun saveChanges() {
        val item = _uiState.value.item ?: return
        val refs = _uiState.value.references
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSaving = true)
            val editedLabels = _uiState.value.editedLabelIds.mapNotNull { labelId ->
                refs?.labels?.find { it.id == labelId }?.let {
                    Label(id = it.id, name = it.name, isCustom = false)
                }
            }
            val updated = item.copy(
                categoryId = _uiState.value.editedCategoryId,
                seasonIds = _uiState.value.editedSeasonIds,
                colorId = _uiState.value.editedColorId,
                materialId = _uiState.value.editedMaterialId,
                labels = editedLabels,
                storagePlace = _uiState.value.editedStoragePlace,
                comment = _uiState.value.editedComment
            )
            updateClothingItemUseCase(item.id, updated, null)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        hasChanges = false,
                        isEditing = false
                    )
                    loadItem()
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isSaving = false,
                        error = error.message ?: "Ошибка сохранения"
                    )
                }
        }
    }
}