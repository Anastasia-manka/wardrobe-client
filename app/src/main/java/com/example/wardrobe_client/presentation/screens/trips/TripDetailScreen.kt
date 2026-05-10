package com.example.wardrobe_client.presentation.screens.trips


import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.wardrobe_client.R
import com.example.wardrobe_client.domain.model.TripItem
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiTextPrimary
import com.example.wardrobe_client.presentation.theme.ShugaiTextSecondary
import com.example.wardrobe_client.presentation.theme.YauzaFont
import com.example.wardrobe_client.presentation.navigation.Screen
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Delete

@Composable
fun TripDetailScreen(
    navController: NavHostController,
    viewModel: TripDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var itemsExpanded by remember { mutableStateOf(true) }
    var selectedItem by remember { mutableStateOf<TripItem?>(null) }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) navController.popBackStack()
    }

    when {
        uiState.isLoading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = ShugaiBluePrimary
                )
            }
        }

        uiState.trip != null -> {
            val trip = uiState.trip!!

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                TripDetailHeader(
                    title = stringResource(R.string.trip_detail_title),
                    onBack = { navController.popBackStack() },
                    onDelete = { viewModel.deleteTrip(context) }
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = null,
                            tint = ShugaiBluePrimary,
                            modifier = Modifier.size(24.dp)
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        MetaChip(
                            label = stringResource(R.string.trip_date_label),
                            value = trip.tripDate
                        )
                        MetaChip(
                            label = stringResource(R.string.trip_type_label),
                            value = trip.tripTypeName
                        )
                        MetaChip(
                            label = stringResource(R.string.trip_climate_label),
                            value = trip.climateName
                        )
                        MetaChip(
                            label = stringResource(R.string.trip_luggage_label),
                            value = trip.luggageTypeName
                        )
                        if (trip.activities.isNotEmpty()) {
                            MetaChip(
                                label = stringResource(R.string.trip_activities_label),
                                value = trip.activities.joinToString(", ") { it.name }
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { itemsExpanded = !itemsExpanded }
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.trip_items_section),
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = ShugaiTextPrimary
                        )
                        Icon(
                            imageVector = if (itemsExpanded)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = ShugaiBluePrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    AnimatedVisibility(visible = itemsExpanded) {
                        if (trip.items.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.trip_items_empty),
                                    fontFamily = InterFont,
                                    fontSize = 16.sp,
                                    color = ShugaiTextSecondary
                                )
                            }
                        } else {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(
                                    start = 24.dp,
                                    end = 24.dp,
                                    top = 8.dp,
                                    bottom = 24.dp
                                ),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier.height(
                                    ((trip.items.size / 2 + trip.items.size % 2) * 217 + 32).dp
                                )
                            ) {
                                items(trip.items) { item ->
                                    TripDetailItemCard(
                                        item = item,
                                        onClick = { selectedItem = item },
                                        onPackedToggle = {
                                            viewModel.updatePackingStatus(
                                                item.itemId,
                                                !item.isPacked
                                            )
                                        }
                                    )
                                }
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
                            navController.navigate(Screen.TripItemsPicker.createRoute(trip.id))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.trip_add_items_button),
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            selectedItem?.let { item ->
                TripItemContextMenu(
                    item = item,
                    onDismiss = { selectedItem = null },
                    onRemove = {
                        viewModel.removeItem(item.itemId)
                        selectedItem = null
                    },
                    onPackedToggle = {
                        viewModel.updatePackingStatus(item.itemId, !item.isPacked)
                        selectedItem = null
                    }
                )
            }
        }
    }
}

@Composable
private fun TripDetailHeader(
    title: String,
    onBack: () -> Unit,
    onDelete: () -> Unit
) {
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
            text = title,
            modifier = Modifier.weight(1f),
            fontFamily = YauzaFont,
            fontSize = 24.sp,
            fontWeight = FontWeight.Normal,
            color = ShugaiBluePrimary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(Color.White, CircleShape)
                .border(1.dp, Color(0xFF8B8B8B), CircleShape)
                .clickable(onClick = onDelete),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                tint = Color(0xFF99111A),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun MetaChip(label: String, value: String) {
    androidx.compose.foundation.text.BasicText(
        text = androidx.compose.ui.text.buildAnnotatedString {
            pushStyle(
                androidx.compose.ui.text.SpanStyle(
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF8B8B8B),
                    fontFamily = InterFont,
                    fontSize = 16.sp
                )
            )
            append(label)
            pop()
            pushStyle(
                androidx.compose.ui.text.SpanStyle(
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF8B8B8B),
                    fontFamily = InterFont,
                    fontSize = 16.sp
                )
            )
            append(" $value")
            pop()
        }
    )
}

@Composable
private fun TripDetailItemCard(
    item: TripItem,
    onClick: () -> Unit,
    onPackedToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(201.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFD9D9D9))
            .clickable(onClick = onPackedToggle)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.categoryName,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (item.isPacked) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x8D003A86))
            )
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun TripItemContextMenu(
    item: TripItem,
    onDismiss: () -> Unit,
    onRemove: () -> Unit,
    onPackedToggle: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(30.dp))
                .background(Color.White)
                .padding(top = 40.dp, bottom = 32.dp, start = 24.dp, end = 24.dp)
        ) {
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = ShugaiTextPrimary
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (item.isPacked)
                        stringResource(R.string.trip_item_mark_unpacked)
                    else
                        stringResource(R.string.trip_item_mark_packed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onPackedToggle)
                        .padding(vertical = 8.dp),
                    fontFamily = InterFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = ShugaiTextPrimary
                )
                Text(
                    text = stringResource(R.string.trip_item_remove),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onRemove)
                        .padding(vertical = 8.dp),
                    fontFamily = InterFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF99111A)
                )
            }
        }
    }
}