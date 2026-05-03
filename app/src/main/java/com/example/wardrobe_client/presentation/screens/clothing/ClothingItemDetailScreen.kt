package com.example.wardrobe_client.presentation.screens.clothing


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobe_client.presentation.navigation.Screen

@Composable
fun ClothingItemDetailScreen(
    navController: NavController,
    viewModel: ClothingItemDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Детали", "Наряды", "С чем носить")

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) navController.popBackStack()
    }

    if (uiState.deleteError != null) {
        AlertDialog(
            onDismissRequest = viewModel::clearDeleteError,
            title = { Text("Невозможно удалить") },
            text = { Text(uiState.deleteError!!) },
            confirmButton = {
                TextButton(onClick = viewModel::clearDeleteError) { Text("OK") }
            }
        )
    }

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val item = uiState.item ?: return

    Column(modifier = Modifier.fillMaxSize()) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.categoryName,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        ScrollableTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTab) {
            0 -> ClothingItemDetailsTab(item = item)
            1 -> ClothingItemOutfitsTab(
                outfits = uiState.outfits,
                onOutfitClick = { outfitId ->
                    navController.navigate(Screen.OutfitDetail.createRoute(outfitId))
                }
            )
            2 -> ClothingItemCompatibleTab(
                compatibleItems = uiState.compatibleItems,
                onSwipeClick = {
                    navController.navigate(Screen.SwipeCompatibility.createRoute(item.id))
                },
                onItemClick = { itemId ->
                    navController.navigate(Screen.ClothingItemDetail.createRoute(itemId))
                }
            )
        }
    }
}