package com.example.wardrobe_client.presentation.screens.clothing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.wardrobe_client.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiPlaceholder
import com.example.wardrobe_client.presentation.theme.ShugaiScreenBackground
import com.example.wardrobe_client.presentation.theme.YauzaFont

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ClothingScreen(
    navController: NavController,
    viewModel: ClothingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var tempCategoryId by remember { mutableStateOf<String?>(null) }
    var tempSeasonId by remember { mutableStateOf<String?>(null) }
    var tempMaterialId by remember { mutableStateOf<String?>(null) }
    var tempLabelId by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ShugaiScreenBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.clothing_title),
                fontFamily = YauzaFont,
                fontSize = 24.sp,
                fontWeight = FontWeight.W400,
                color = ShugaiBluePrimary,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .border(1.dp, ShugaiPlaceholder, RoundedCornerShape(30.dp))
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = ShugaiPlaceholder,
                        modifier = Modifier.size(24.dp)
                    )
                    androidx.compose.material3.TextField(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.onSearchQuery(it)
                        },
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_placeholder),
                                fontFamily = InterFont,
                                fontSize = 20.sp,
                                color = ShugaiPlaceholder
                            )
                        },
                        singleLine = true,
                        colors = androidx.compose.material3.TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .border(1.dp, ShugaiPlaceholder, RoundedCornerShape(30.dp))
                        .clickable { showFilterSheet = true },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Фильтры",
                        tint = ShugaiPlaceholder,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.CircularProgressIndicator(
                            color = ShugaiBluePrimary
                        )
                    }
                }
                uiState.items.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.clothing_empty),
                            fontFamily = InterFont,
                            color = ShugaiPlaceholder
                        )
                    }
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            start = 24.dp,
                            end = 24.dp,
                            bottom = 100.dp
                        ),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.filteredItems) { item ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(174f / 201f)
                                    .clip(RoundedCornerShape(30.dp))
                                    .background(Color(0xFFD9D9D9))
                                    .clickable {
                                        navController.navigate(
                                            Screen.ClothingItemDetail.createRoute(item.id)
                                        )
                                    }
                            ) {
                                AsyncImage(
                                    model = item.imageUrl,
                                    contentDescription = item.categoryName,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showFilterSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilterSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(24.dp))
                        Text(
                            text = stringResource(R.string.filter_title),
                            fontFamily = YauzaFont,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.W400,
                            color = ShugaiBluePrimary
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = ShugaiBluePrimary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showFilterSheet = false }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    uiState.references?.let { refs ->
                        Text(
                            text = stringResource(R.string.filter_categories),
                            fontFamily = YauzaFont,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            refs.categoryGroups.forEach { group ->
                                group.categories.forEach { cat ->
                                    FilterChip(
                                        label = cat.name,
                                        selected = tempCategoryId == cat.id,
                                        onClick = {
                                            tempCategoryId =
                                                if (tempCategoryId == cat.id) null else cat.id
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.clothing_season_label),
                            fontFamily = YauzaFont,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            refs.seasons.forEach { season ->
                                FilterChip(
                                    label = season.name,
                                    selected = tempSeasonId == season.id,
                                    onClick = {
                                        tempSeasonId =
                                            if (tempSeasonId == season.id) null else season.id
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.clothing_material_label),
                            fontFamily = YauzaFont,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            refs.materials.forEach { material ->
                                FilterChip(
                                    label = material.name,
                                    selected = tempMaterialId == material.id,
                                    onClick = {
                                        tempMaterialId =
                                            if (tempMaterialId == material.id) null else material.id
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = stringResource(R.string.clothing_labels_label),
                            fontFamily = YauzaFont,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            refs.labels.forEach { label ->
                                FilterChip(
                                    label = label.name,
                                    selected = tempLabelId == label.id,
                                    onClick = {
                                        tempLabelId =
                                            if (tempLabelId == label.id) null else label.id
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(51.dp)
                                .border(1.dp, ShugaiBluePrimary, RoundedCornerShape(30.dp))
                                .clickable {
                                    tempCategoryId = null
                                    tempSeasonId = null
                                    tempMaterialId = null
                                    tempLabelId = null
                                    viewModel.clearFilters()
                                    showFilterSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.filter_reset),
                                fontFamily = InterFont,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = ShugaiBluePrimary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(51.dp)
                                .background(ShugaiBluePrimary, RoundedCornerShape(30.dp))
                                .clickable {
                                    viewModel.onCategoryFilter(tempCategoryId)
                                    viewModel.onSeasonFilter(tempSeasonId)
                                    viewModel.onMaterialFilter(tempMaterialId)
                                    viewModel.onLabelFilter(tempLabelId)
                                    showFilterSheet = false
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.onboarding_apply),
                                fontFamily = InterFont,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .height(36.dp)
            .border(
                width = 1.dp,
                color = if (selected) ShugaiBluePrimary else ShugaiPlaceholder,
                shape = RoundedCornerShape(30.dp)
            )
            .background(
                color = if (selected) ShugaiBluePrimary else Color.Transparent,
                shape = RoundedCornerShape(30.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontFamily = InterFont,
            fontSize = 14.sp,
            color = if (selected) Color.White else Color.Black
        )
    }
}