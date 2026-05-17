package com.example.wardrobe_client.presentation.screens.search

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.repository.VisualSearchGroup
import com.example.wardrobe_client.domain.repository.VisualSearchResult
import com.example.wardrobe_client.domain.usecase.search.VisualSearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

data class VisualSearchUiState(
    val selectedImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val grouped: Boolean = false,
    val items: List<ClothingItem> = emptyList(),
    val groups: List<VisualSearchGroup> = emptyList(),
    val hasSearched: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class VisualSearchViewModel @Inject constructor(
    private val visualSearchUseCase: VisualSearchUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(VisualSearchUiState())
    val uiState: StateFlow<VisualSearchUiState> = _uiState.asStateFlow()

    fun onImageSelected(uri: Uri) {
        _uiState.update {
            it.copy(
                selectedImageUri = uri,
                items = emptyList(),
                groups = emptyList(),
                hasSearched = false,
                error = null
            )
        }
        search(uri)
    }

    private fun search(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val file = uriToFile(uri)
                val result = visualSearchUseCase(file)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        grouped = result.grouped,
                        items = result.items,
                        groups = result.groups,
                        hasSearched = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        hasSearched = true,
                        error = e.message
                    )
                }
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException("Cannot open image")
        val tempFile = File.createTempFile("visual_search_", ".jpg", context.cacheDir)
        FileOutputStream(tempFile).use { output ->
            inputStream.copyTo(output)
        }
        return tempFile
    }

    fun clearSearch() {
        _uiState.update { VisualSearchUiState() }
    }
}