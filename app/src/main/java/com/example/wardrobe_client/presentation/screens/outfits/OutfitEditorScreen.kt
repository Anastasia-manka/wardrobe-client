package com.example.wardrobe_client.presentation.screens.outfits

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlin.math.roundToInt

@Composable
fun OutfitEditorScreen(
    navController: NavController,
    viewModel: OutfitEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var canvasSize by remember { mutableStateOf(Offset(1f, 1f)) }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) navController.popBackStack()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Color.LightGray)
                .onSizeChanged { size ->
                    canvasSize = Offset(size.width.toFloat(), size.height.toFloat())
                }
        ) {
            uiState.canvasItems.forEachIndexed { index, canvasItem ->
                var scale by remember { mutableFloatStateOf(canvasItem.scale) }

                AsyncImage(
                    model = canvasItem.clothingItem.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size((100 * scale).dp)
                        .offset {
                            IntOffset(
                                (canvasItem.x * canvasSize.x).roundToInt(),
                                (canvasItem.y * canvasSize.y).roundToInt()
                            )
                        }
                        .pointerInput(index) {
                            detectTransformGestures { _, pan, zoom, _ ->
                                scale = (scale * zoom).coerceIn(0.3f, 3f)
                                val newX = (canvasItem.x + pan.x / canvasSize.x).coerceIn(0f, 1f)
                                val newY = (canvasItem.y + pan.y / canvasSize.y).coerceIn(0f, 1f)
                                viewModel.updateItemPosition(index, newX, newY)
                                viewModel.updateItemScale(index, scale)
                            }
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text("Вещи (нажми чтобы добавить на холст):")

        uiState.availableItems.take(5).forEach { item ->
            Button(
                onClick = { viewModel.addItemToCanvas(item) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(item.categoryName)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { viewModel.save("") },
            enabled = !uiState.isLoading && uiState.canvasItems.isNotEmpty(),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.isLoading) CircularProgressIndicator()
            else Text("Сохранить наряд")
        }
    }
}