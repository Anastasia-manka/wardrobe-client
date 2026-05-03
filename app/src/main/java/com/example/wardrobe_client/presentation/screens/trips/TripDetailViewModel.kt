package com.example.wardrobe_client.presentation.screens.trips

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wardrobe_client.domain.model.Trip
import com.example.wardrobe_client.domain.usecase.trip.AddItemToTripUseCase
import com.example.wardrobe_client.domain.usecase.trip.DeleteTripUseCase
import com.example.wardrobe_client.domain.usecase.trip.GetTripUseCase
import com.example.wardrobe_client.domain.usecase.trip.RemoveItemFromTripUseCase
import com.example.wardrobe_client.domain.usecase.trip.UpdatePackingStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TripDetailUiState(
    val trip: Trip? = null,
    val isLoading: Boolean = false,
    val isDeleted: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TripDetailViewModel @Inject constructor(
    private val getTripUseCase: GetTripUseCase,
    private val deleteTripUseCase: DeleteTripUseCase,
    private val addItemToTripUseCase: AddItemToTripUseCase,
    private val updatePackingStatusUseCase: UpdatePackingStatusUseCase,
    private val removeItemFromTripUseCase: RemoveItemFromTripUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val tripId: String = checkNotNull(savedStateHandle["tripId"])

    private val _uiState = MutableStateFlow(TripDetailUiState())
    val uiState: StateFlow<TripDetailUiState> = _uiState.asStateFlow()

    init {
        loadTrip()
    }

    fun loadTrip() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            getTripUseCase(tripId)
                .onSuccess { trip ->
                    _uiState.value = _uiState.value.copy(isLoading = false, trip = trip)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message ?: "Ошибка загрузки"
                    )
                }
        }
    }

    fun deleteTrip() {
        viewModelScope.launch {
            deleteTripUseCase(tripId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isDeleted = true)
                }
        }
    }

    fun updatePackingStatus(itemId: String, isPacked: Boolean) {
        viewModelScope.launch {
            updatePackingStatusUseCase(tripId, itemId, isPacked)
                .onSuccess { loadTrip() }
        }
    }

    fun removeItem(itemId: String) {
        viewModelScope.launch {
            removeItemFromTripUseCase(tripId, itemId)
                .onSuccess { loadTrip() }
        }
    }
}