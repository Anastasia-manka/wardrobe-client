package com.example.wardrobe_client.presentation.screens.trips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.wardrobe_client.R
import com.example.wardrobe_client.domain.model.Trip
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.YauzaFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiTextPrimary
import com.example.wardrobe_client.presentation.theme.ShugaiTextSecondary
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work
import androidx.navigation.NavHostController
import com.example.wardrobe_client.presentation.navigation.Screen
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TripsScreen(
    navController: NavHostController,
    viewModel: TripsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = stringResource(R.string.trips_screen_title),
                fontFamily = YauzaFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
                color = ShugaiBluePrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = ShugaiBluePrimary
                        )
                    }
                    uiState.error != null -> {
                        Text(
                            text = uiState.error ?: "",
                            modifier = Modifier.align(Alignment.Center).padding(horizontal = 24.dp),
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = ShugaiTextSecondary
                        )
                    }
                    uiState.trips.isEmpty() -> {
                        Text(
                            text = stringResource(R.string.trips_empty),
                            modifier = Modifier.align(Alignment.Center).padding(horizontal = 24.dp),
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = ShugaiTextSecondary
                        )
                    }
                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(
                                start = 24.dp, end = 24.dp, top = 16.dp, bottom = 100.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.trips) { trip ->
                                TripCard(
                                    trip = trip,
                                    onClick = {
                                        navController.navigate(Screen.TripDetail.createRoute(trip.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TripCard(
    trip: Trip,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color(0xFF8B8B8B))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Work,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = ShugaiBluePrimary
                )
                Text(
                    text = trip.name,
                    fontFamily = InterFont,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = ShugaiTextPrimary
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                MetaText(
                    label = stringResource(R.string.trip_type_label),
                    value = trip.tripTypeName
                )
                MetaText(
                    label = stringResource(R.string.trip_climate_label),
                    value = trip.climateName
                )
                if (trip.tripDate.isNotBlank()) {
                    MetaText(
                        label = stringResource(R.string.trip_date_label),
                        value = trip.tripDate
                    )
                }
            }

        }
    }
}

@Composable
private fun MetaText(label: String, value: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(label) }
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.Medium)) { append(value) }
        },
        fontFamily = InterFont,
        fontSize = 14.sp,
        color = Color(0xFF8B8B8B)
    )
}