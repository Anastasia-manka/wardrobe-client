package com.example.wardrobe_client.presentation.screens.trips

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.wardrobe_client.R
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiTextPrimary
import com.example.wardrobe_client.presentation.theme.YauzaFont
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.filled.ArrowBackIosNew

@Composable
fun TripItemsPickerScreen(
    navController: NavHostController,
    viewModel: TripItemsPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filtered = viewModel.filteredItems()

    Column(modifier = Modifier.fillMaxSize()) {
        TripItemsPickerHeader(onBack = { navController.popBackStack() })

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = ShugaiBluePrimary
                    )
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        start = 24.dp,
                        end = 24.dp,
                        top = 16.dp,
                        bottom = 100.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filtered) { item ->
                        PickerItemCard(
                            item = item,
                            isAdded = item.id in uiState.addedItemIds,
                            onClick = { viewModel.toggleItem(item.id) }
                        )
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .height(51.dp)
                .background(ShugaiBluePrimary, RoundedCornerShape(30.dp))
                .clickable {
                    navController.navigate(Screen.TripDetail.createRoute(viewModel.getTripId())) {
                        popUpTo(Screen.AddTrip.route) { inclusive = true }
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.trip_picker_done_button),
                fontFamily = InterFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
private fun TripItemsPickerHeader(onBack: () -> Unit) {
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
            text = stringResource(R.string.trip_picker_title),
            modifier = Modifier.weight(1f),
            fontFamily = YauzaFont,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = ShugaiBluePrimary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.size(24.dp))
    }
}

@Composable
private fun PickerItemCard(
    item: ClothingItem,
    isAdded: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(201.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFD9D9D9))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.categoryName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        if (isAdded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x8D003A86))
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(30.dp)
                .background(
                    if (isAdded) ShugaiBluePrimary else Color.Transparent,
                    CircleShape
                )
                .border(
                    1.5.dp,
                    if (isAdded) ShugaiBluePrimary else Color(0xFF8B8B8B),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isAdded) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}