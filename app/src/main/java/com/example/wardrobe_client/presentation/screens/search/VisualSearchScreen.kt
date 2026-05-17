package com.example.wardrobe_client.presentation.screens.search

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobe_client.R
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.repository.VisualSearchGroup
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiPlaceholder
import com.example.wardrobe_client.presentation.theme.ShugaiScreenBackground
import com.example.wardrobe_client.presentation.theme.ShugaiTextSecondary
import com.example.wardrobe_client.presentation.theme.YauzaFont

@Composable
fun VisualSearchScreen(
    navController: NavController,
    viewModel: VisualSearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.onImageSelected(it) } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ShugaiScreenBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = stringResource(R.string.visual_search_title),
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
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (uiState.selectedImageUri != null) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .border(1.dp, Color(0xFFB00020), CircleShape)
                            .clickable { viewModel.clearSearch() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color(0xFFB00020),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .border(1.dp, ShugaiBluePrimary, RoundedCornerShape(30.dp))
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = ShugaiBluePrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(R.string.visual_search_upload_button),
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            color = ShugaiBluePrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = ShugaiBluePrimary)
                    }
                }

                uiState.selectedImageUri != null -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 24.dp)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(30.dp))
                                    .background(Color(0xFFD9D9D9))
                            ) {
                                AsyncImage(
                                    model = uiState.selectedImageUri,
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        when {
                            uiState.hasSearched && uiState.groups.isEmpty() && uiState.items.isEmpty() -> {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 32.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.visual_search_empty),
                                            fontFamily = InterFont,
                                            fontSize = 16.sp,
                                            color = ShugaiPlaceholder
                                        )
                                    }
                                }
                            }

                            uiState.grouped && uiState.groups.isNotEmpty() -> {
                                items(uiState.groups) { group ->
                                    SearchGroupSection(
                                        group = group,
                                        onItemClick = { item ->
                                            navController.navigate(
                                                Screen.ClothingItemDetail.createRoute(item.id)
                                            )
                                        }
                                    )
                                }
                            }

                            uiState.items.isNotEmpty() -> {
                                item {
                                    Text(
                                        text = stringResource(R.string.visual_search_results_label),
                                        fontFamily = InterFont,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W500,
                                        color = Color.Black,
                                        modifier = Modifier.padding(horizontal = 24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                }
                                val chunked = uiState.items.chunked(2)
                                items(chunked) { rowItems ->
                                    SearchItemRow(
                                        rowItems = rowItems,
                                        onItemClick = { item ->
                                            navController.navigate(
                                                Screen.ClothingItemDetail.createRoute(item.id)
                                            )
                                        }
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchGroupSection(
    group: VisualSearchGroup,
    onItemClick: (ClothingItem) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = group.categoryGroup,
            fontFamily = InterFont,
            fontSize = 16.sp,
            fontWeight = FontWeight.W600,
            color = ShugaiTextSecondary,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        val chunked = group.items.chunked(2)
        chunked.forEach { rowItems ->
            SearchItemRow(rowItems = rowItems, onItemClick = onItemClick)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun SearchItemRow(
    rowItems: List<ClothingItem>,
    onItemClick: (ClothingItem) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        rowItems.forEach { item ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(174f / 201f)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFD9D9D9))
                    .clickable { onItemClick(item) }
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        repeat(2 - rowItems.size) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}