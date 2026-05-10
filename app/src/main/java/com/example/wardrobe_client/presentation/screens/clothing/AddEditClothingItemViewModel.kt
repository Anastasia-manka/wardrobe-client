package com.example.wardrobe_client.presentation.screens.clothing

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
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
import com.example.wardrobe_client.domain.usecase.clothing.UploadImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

data class AddEditClothingItemUiState(
    val photoUri: Uri? = null,
    val existingImageUrl: String = "",
    val categoryId: String = "",
    val seasonId: String = "",
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
    @ApplicationContext private val context: Context,
    private val createClothingItemUseCase: CreateClothingItemUseCase,
    private val updateClothingItemUseCase: UpdateClothingItemUseCase,
    private val getClothingItemUseCase: GetClothingItemUseCase,
    private val getReferencesUseCase: GetReferencesUseCase,
    private val uploadImageUseCase: UploadImageUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val itemId: String? = savedStateHandle["itemId"]
    val isEditMode = itemId != null

    private val _uiState = MutableStateFlow(AddEditClothingItemUiState())
    val uiState: StateFlow<AddEditClothingItemUiState> = _uiState.asStateFlow()

    private var pendingCameraUri: Uri? = null

    init {
        loadReferences()
        if (isEditMode) loadItem()
    }

    private fun loadReferences() {
        viewModelScope.launch {
            getReferencesUseCase()
                .onSuccess { refs ->
                    _uiState.value = _uiState.value.copy(references = refs)
                }
        }
    }

    private fun loadItem() {
        viewModelScope.launch {
            getClothingItemUseCase(itemId!!)
                .onSuccess { item ->
                    _uiState.value = _uiState.value.copy(
                        existingImageUrl = item.imageUrl,
                        categoryId = item.categoryId,
                        seasonId = item.seasonIds.firstOrNull() ?: "",
                        colorId = item.colorId,
                        materialId = item.materialId,
                        labels = item.labels,
                        storagePlace = item.storagePlace,
                        comment = item.comment
                    )
                }
        }
    }

    fun createCameraUri(): Uri {
        val file = File(context.cacheDir, "photo_${System.currentTimeMillis()}.jpg")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
        pendingCameraUri = uri
        return uri
    }

    fun onCameraPhotoTaken() {
        pendingCameraUri?.let { uri ->
            _uiState.value = _uiState.value.copy(photoUri = uri, error = null)
        }
    }

    fun onPhotoSelected(uri: Uri) {
        _uiState.value = _uiState.value.copy(photoUri = uri, error = null)
    }

    fun onCategoryChange(categoryId: String) {
        _uiState.value = _uiState.value.copy(categoryId = categoryId)
    }

    fun onSeasonChange(seasonId: String) {
        _uiState.value = _uiState.value.copy(seasonId = seasonId)
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
        val state = _uiState.value

        if (!isEditMode && state.photoUri == null) {
            _uiState.value = state.copy(error = "Добавьте фото вещи")
            return
        }
        if (state.categoryId.isBlank()) {
            _uiState.value = state.copy(error = "Выберите категорию")
            return
        }
        if (state.seasonId.isBlank()) {
            _uiState.value = state.copy(error = "Выберите сезон")
            return
        }
        if (state.colorId.isBlank()) {
            _uiState.value = state.copy(error = "Выберите цвет")
            return
        }
        if (state.materialId.isBlank()) {
            _uiState.value = state.copy(error = "Выберите материал")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val imageUrl = if (state.photoUri != null) {
                uploadImageUseCase(state.photoUri)
                    .getOrElse { e ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Ошибка загрузки фото: ${e.message}"
                        )
                        return@launch
                    }
            } else {
                state.existingImageUrl
            }

            val item = ClothingItem(
                id = itemId ?: "",
                imageUrl = imageUrl,
                categoryId = state.categoryId,
                categoryName = "",
                categoryGroupName = "",
                seasonIds = listOfNotNull(state.seasonId.ifBlank { null }),
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
                updateClothingItemUseCase(itemId!!, item, imageUrl)
            } else {
                createClothingItemUseCase(item, imageUrl)
            }

            result
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Ошибка сохранения"
                    )
                }
        }
    }
}