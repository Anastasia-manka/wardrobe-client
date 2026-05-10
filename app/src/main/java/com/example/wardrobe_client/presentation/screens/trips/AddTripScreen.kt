package com.example.wardrobe_client.presentation.screens.trips

import android.app.DatePickerDialog
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.wardrobe_client.R
import com.example.wardrobe_client.domain.model.ReferenceItem
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiTextPrimary
import com.example.wardrobe_client.presentation.theme.ShugaiTextSecondary
import java.util.Calendar
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import com.example.wardrobe_client.presentation.theme.YauzaFont

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTripScreen(
    navController: NavHostController,
    viewModel: AddTripViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.savedTripId) {
        uiState.savedTripId?.let { tripId ->
            navController.navigate(Screen.TripItemsPicker.createRoute(tripId)) {
                popUpTo(Screen.AddTrip.route) { inclusive = true }
            }
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.ic_flowers_3),
            contentDescription = null,
            modifier = Modifier
                .requiredSize(500.dp)
                .offset(x = (-80).dp, y = 250.dp),
            alpha = 0.1f
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AddTripHeader(onBack = { navController.popBackStack() })

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TripTextField(
                    value = uiState.name,
                    placeholder = stringResource(R.string.trip_name_placeholder),
                    onValueChange = viewModel::onNameChange
                )

                TripDateField(
                    value = uiState.tripDate,
                    placeholder = stringResource(R.string.trip_date_placeholder),
                    onDateSelected = viewModel::onDateChange
                )

                uiState.references?.let { refs ->
                    TripDropdownField(
                        value = refs.tripTypes.find { it.id == uiState.tripTypeId }?.name ?: "",
                        placeholder = stringResource(R.string.trip_type_placeholder),
                        items = refs.tripTypes,
                        onItemSelected = { viewModel.onTripTypeChange(it.id) }
                    )

                    TripDropdownField(
                        value = refs.climates.find { it.id == uiState.climateId }?.name ?: "",
                        placeholder = stringResource(R.string.trip_climate_placeholder),
                        items = refs.climates,
                        onItemSelected = { viewModel.onClimateChange(it.id) }
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.trip_activities_label),
                            fontFamily = InterFont,
                            fontSize = 14.sp,
                            color = ShugaiTextSecondary
                        )
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            refs.activities.forEach { activity ->
                                val selected = uiState.selectedActivities.any { it.id == activity.id }
                                ActivityChip(
                                    label = activity.name,
                                    selected = selected,
                                    onClick = { viewModel.onActivityToggle(activity) }
                                )
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.trip_luggage_label),
                            fontFamily = InterFont,
                            fontSize = 14.sp,
                            color = ShugaiTextSecondary
                        )
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            refs.luggageTypes.forEach { luggage ->
                                val selected = uiState.luggageTypeId == luggage.id
                                ActivityChip(
                                    label = luggage.name,
                                    selected = selected,
                                    onClick = { viewModel.onLuggageTypeChange(luggage.id) }
                                )
                            }
                        }
                    }
                }

                if (uiState.error != null) {
                    Text(
                        text = uiState.error ?: "",
                        fontFamily = InterFont,
                        fontSize = 14.sp,
                        color = Color(0xFF99111A)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ShugaiBluePrimary
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(51.dp)
                            .background(ShugaiBluePrimary, RoundedCornerShape(30.dp))
                            .clickable { viewModel.save(context) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.trip_continue_button),
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AddTripHeader(onBack: () -> Unit) {
    Spacer(modifier = Modifier.height(48.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBackIosNew,
            contentDescription = null,
            tint = ShugaiBluePrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onBack)
        )
        Text(
            text = stringResource(R.string.add_trip_title),
            modifier = Modifier.weight(1f),
            fontFamily = YauzaFont,
            fontSize = 24.sp,
            fontWeight = FontWeight.W400,
            color = ShugaiBluePrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.size(24.dp))
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun TripTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                fontFamily = InterFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8B8B8B)
            )
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color(0xFFD9D9D9),
            focusedBorderColor = ShugaiBluePrimary,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
private fun TripDateField(
    value: String,
    placeholder: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePicker = remember {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                onDateSelected("%04d-%02d-%02d".format(year, month + 1, day))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(51.dp)
            .border(1.5.dp, Color(0xFFD9D9D9), RoundedCornerShape(30.dp))
            .background(Color.White, RoundedCornerShape(30.dp))
            .clickable { datePicker.show() }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (value.isBlank()) placeholder else value,
            fontFamily = InterFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (value.isBlank()) Color(0xFF8B8B8B) else ShugaiTextPrimary
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color(0xFF8B8B8B),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun TripDropdownField(
    value: String,
    placeholder: String,
    items: List<ReferenceItem>,
    onItemSelected: (ReferenceItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(51.dp)
                .border(1.5.dp, Color(0xFFD9D9D9), RoundedCornerShape(30.dp))
                .background(Color.White, RoundedCornerShape(30.dp))
                .clickable { expanded = true }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = if (value.isBlank()) placeholder else value,
                fontFamily = InterFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = if (value.isBlank()) Color(0xFF8B8B8B) else ShugaiTextPrimary
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = Color(0xFF8B8B8B),
                modifier = Modifier.size(24.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item.name,
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = Color(0xFF1A1A1A)
                        )
                    },
                    onClick = {
                        onItemSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun ActivityChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(
                BorderStroke(1.dp, if (selected) ShugaiBluePrimary else Color(0xFFB4B0B0)),
                RoundedCornerShape(30.dp)
            )
            .background(
                if (selected) ShugaiBluePrimary else Color.White,
                RoundedCornerShape(30.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = label,
            fontFamily = InterFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = if (selected) Color.White else ShugaiTextPrimary
        )
    }
}