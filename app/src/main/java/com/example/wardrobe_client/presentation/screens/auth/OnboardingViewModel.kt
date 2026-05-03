package com.example.wardrobe_client.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.TemplateItem
import com.example.wardrobe_client.domain.usecase.clothing.CreateItemsFromTemplatesUseCase
import com.example.wardrobe_client.domain.usecase.clothing.GetTemplateItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OnboardingUiState(
    val templateItems: List<TemplateItem> = emptyList(),
    val selectedIds: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getTemplateItemsUseCase: GetTemplateItemsUseCase,
    private val createItemsFromTemplatesUseCase: CreateItemsFromTemplatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        loadTemplates()
    }

    private fun loadTemplates() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getTemplateItemsUseCase()
                .onSuccess { items ->
                    _uiState.value = _uiState.value.copy(isLoading = false, templateItems = items)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
        }
    }

    fun toggleSelection(id: String) {
        val current = _uiState.value.selectedIds.toMutableSet()
        if (current.contains(id)) current.remove(id) else current.add(id)
        _uiState.value = _uiState.value.copy(selectedIds = current)
    }

    fun confirm() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            createItemsFromTemplatesUseCase(_uiState.value.selectedIds.toList())
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isSuccess = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка"
                    )
                }
        }
    }

    fun skip() {
        _uiState.value = _uiState.value.copy(isSuccess = true)
    }
}