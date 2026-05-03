package com.example.wardrobe_client.presentation.screens.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.ReferenceItem
import com.example.wardrobe_client.domain.model.References
import com.example.wardrobe_client.domain.model.Trip
import com.example.wardrobe_client.domain.model.TripItem
import com.example.wardrobe_client.domain.usecase.GetReferencesUseCase
import com.example.wardrobe_client.domain.usecase.trip.CreateTripUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTripUiState(
    val name: String = "",
    val tripDate: String = "",
    val tripTypeId: String = "",
    val climateId: String = "",
    val luggageTypeId: String = "",
    val selectedActivities: List<ReferenceItem> = emptyList(),
    val references: References? = null,
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddTripViewModel @Inject constructor(
    private val createTripUseCase: CreateTripUseCase,
    private val getReferencesUseCase: GetReferencesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTripUiState())
    val uiState: StateFlow<AddTripUiState> = _uiState.asStateFlow()

    init {
        loadReferences()
    }

    private fun loadReferences() {
        viewModelScope.launch {
            getReferencesUseCase()
                .onSuccess { references ->
                    _uiState.value = _uiState.value.copy(references = references)
                }
        }
    }

    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(name = name)
    }

    fun onDateChange(date: String) {
        _uiState.value = _uiState.value.copy(tripDate = date)
    }

    fun onTripTypeChange(id: String) {
        _uiState.value = _uiState.value.copy(tripTypeId = id)
    }

    fun onClimateChange(id: String) {
        _uiState.value = _uiState.value.copy(climateId = id)
    }

    fun onLuggageTypeChange(id: String) {
        _uiState.value = _uiState.value.copy(luggageTypeId = id)
    }

    fun onActivityToggle(activity: ReferenceItem) {
        val current = _uiState.value.selectedActivities.toMutableList()
        if (current.any { it.id == activity.id }) current.removeAll { it.id == activity.id }
        else current.add(activity)
        _uiState.value = _uiState.value.copy(selectedActivities = current)
    }

    fun save() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val state = _uiState.value
            val trip = Trip(
                id = "",
                name = state.name,
                tripDate = state.tripDate,
                tripTypeId = state.tripTypeId,
                tripTypeName = "",
                climateId = state.climateId,
                climateName = "",
                luggageTypeId = state.luggageTypeId,
                luggageTypeName = "",
                activities = state.selectedActivities,
                items = emptyList()
            )
            createTripUseCase(trip)
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