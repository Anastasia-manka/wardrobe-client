package com.example.wardrobe_client.presentation.screens.outfits

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.data.remote.CloudinaryUploader
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.OutfitItem
import com.example.wardrobe_client.domain.model.References
import com.example.wardrobe_client.domain.usecase.GetReferencesUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetClothingItemsUseCase
import com.example.wardrobe_client.domain.usecase.outfit.CreateOutfitUseCase
import com.example.wardrobe_client.domain.usecase.outfit.GetOutfitUseCase
import com.example.wardrobe_client.domain.usecase.outfit.UpdateOutfitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
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
    @ApplicationContext private val context: Context,
    private val createOutfitUseCase: CreateOutfitUseCase,
    private val updateOutfitUseCase: UpdateOutfitUseCase,
    private val getOutfitUseCase: GetOutfitUseCase,
    private val getClothingItemsUseCase: GetClothingItemsUseCase,
    private val getReferencesUseCase: GetReferencesUseCase,
    private val cloudinaryUploader: CloudinaryUploader,
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
                .onSuccess { refs ->
                    _uiState.value = _uiState.value.copy(references = refs)
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
                    val canvasItems = outfit.items.mapNotNull { outfitItem ->
                        val clothingItem = _uiState.value.availableItems
                            .find { it.id == outfitItem.itemId }
                        clothingItem?.let {
                            CanvasItem(
                                clothingItem = it,
                                x = outfitItem.x,
                                y = outfitItem.y,
                                scale = outfitItem.scale
                            )
                        }
                    }
                    _uiState.value = _uiState.value.copy(
                        selectedStyleId = outfit.styleId,
                        canvasItems = canvasItems
                    )
                }
        }
    }

    fun addItemToCanvas(item: ClothingItem) {
        val current = _uiState.value.canvasItems.toMutableList()
        current.add(CanvasItem(clothingItem = item, x = 0.1f, y = 0.1f, scale = 1f))
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

    fun saveWithCapture(canvasSize: Offset) {
        android.util.Log.d("OutfitEditor", "canvasSize x=${canvasSize.x} y=${canvasSize.y}")
        val state = _uiState.value

        if (state.canvasItems.isEmpty()) {
            _uiState.value = state.copy(error = "Добавьте хотя бы одну вещь на холст")
            return
        }
        if (state.selectedStyleId.isBlank()) {
            _uiState.value = state.copy(error = "Выберите стиль наряда")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val bitmap = withContext(Dispatchers.IO) {
                renderCanvasToBitmap(state.canvasItems, canvasSize)
            }

            val coverUrl = uploadBitmap(bitmap) ?: run {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка загрузки обложки"
                )
                return@launch
            }

            val items = state.canvasItems.map {
                OutfitItem(
                    itemId = it.clothingItem.id,
                    x = it.x,
                    y = it.y,
                    scale = it.scale
                )
            }

            val result = if (isEditMode) {
                updateOutfitUseCase(outfitId!!, coverUrl, state.selectedStyleId, items)
            } else {
                createOutfitUseCase(coverUrl, state.selectedStyleId, items)
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

    private suspend fun renderCanvasToBitmap(
        items: List<CanvasItem>,
        canvasSize: Offset
    ): Bitmap = withContext(Dispatchers.IO) {
        val outputSize = 1080
        val bitmap = Bitmap.createBitmap(outputSize, outputSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)

        val imageLoader = coil.ImageLoader(context)
        val scaleX = outputSize / canvasSize.x
        val scaleY = outputSize / canvasSize.y

        items.forEach { canvasItem ->
            try {
                val request = coil.request.ImageRequest.Builder(context)
                    .data(canvasItem.clothingItem.imageUrl)
                    .allowHardware(false)
                    .build()
                val result = imageLoader.execute(request)
                if (result is coil.request.SuccessResult) {
                    val drawable = result.drawable
                    val sourceBitmap = (drawable as android.graphics.drawable.BitmapDrawable).bitmap

                    val itemSizePx = (canvasItem.scale * outputSize * 0.26f).toInt().coerceAtLeast(1)
                    val x = (canvasItem.x * outputSize)
                    val y = (canvasItem.y * outputSize)

                    val sourceWidth = sourceBitmap.width
                    val sourceHeight = sourceBitmap.height
                    val aspectRatio = sourceWidth.toFloat() / sourceHeight.toFloat()

                    val scaledWidth: Int
                    val scaledHeight: Int
                    if (aspectRatio >= 1f) {
                        scaledWidth = itemSizePx
                        scaledHeight = (itemSizePx / aspectRatio).toInt().coerceAtLeast(1)
                    } else {
                        scaledHeight = itemSizePx
                        scaledWidth = (itemSizePx * aspectRatio).toInt().coerceAtLeast(1)
                    }

                    val scaled = Bitmap.createScaledBitmap(sourceBitmap, scaledWidth, scaledHeight, true)
                    canvas.drawBitmap(scaled, x, y, null)
                    scaled.recycle()
                }
            } catch (e: Exception) {
                android.util.Log.e("OutfitEditor", "Failed to draw item: ${canvasItem.clothingItem.id}", e)
            }
        }
        bitmap
    }

    private suspend fun uploadBitmap(bitmap: Bitmap): String? {
        return withContext(Dispatchers.IO) {
            runCatching {
                val file = File(context.cacheDir, "outfit_cover_${System.currentTimeMillis()}.jpg")
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                file.writeBytes(stream.toByteArray())
                val uri = android.net.Uri.fromFile(file)
                cloudinaryUploader.uploadImage(uri).getOrNull()
            }.getOrNull()
        }
    }
}