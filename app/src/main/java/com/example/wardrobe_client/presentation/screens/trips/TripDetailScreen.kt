package com.example.wardrobe_client.presentation.screens.trips

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun TripDetailScreen(
    navController: NavController,
    viewModel: TripDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) navController.popBackStack()
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val trip = uiState.trip ?: return

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Поездка: ${trip.name}")
        Spacer(modifier = Modifier.height(4.dp))
        Text("Дата: ${trip.tripDate}")
        Spacer(modifier = Modifier.height(4.dp))
        Text("Тип: ${trip.tripTypeName}")
        Spacer(modifier = Modifier.height(4.dp))
        Text("Климат: ${trip.climateName}")
        Spacer(modifier = Modifier.height(4.dp))
        Text("Багаж: ${trip.luggageTypeName}")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Чек-лист вещей:")

        LazyColumn {
            items(trip.items) { tripItem ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = tripItem.isPacked,
                        onCheckedChange = { checked ->
                            viewModel.updatePackingStatus(tripItem.itemId, checked)
                        }
                    )
                    Text(tripItem.itemId)
                }
            }
        }
    }
}