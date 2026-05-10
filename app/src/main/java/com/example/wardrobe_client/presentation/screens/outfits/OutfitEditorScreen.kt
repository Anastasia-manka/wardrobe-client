package com.example.wardrobe_client.presentation.screens.outfits

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.presentation.screens.clothing.ItemFieldPickerScreen
import com.example.wardrobe_client.presentation.screens.clothing.PickerItem
import com.example.wardrobe_client.presentation.theme.*
import kotlin.math.roundToInt
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity

@Composable
fun OutfitEditorScreen(
    navController: NavController,
    viewModel: OutfitEditorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var canvasSize by remember { mutableStateOf(Offset(1f, 1f)) }
    var showStylePicker by remember { mutableStateOf(false) }
    var captureCanvas by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) navController.popBackStack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ShugaiScreenBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = null,
                    tint = ShugaiBluePrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )
                Text(
                    text = if (viewModel.isEditMode) "Редактировать наряд" else "Новый наряд",
                    fontFamily = YauzaFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400,
                    color = ShugaiBluePrimary
                )
                Spacer(modifier = Modifier.size(24.dp))
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .background(Color.White)
                    .clipToBounds()
                    .onSizeChanged { size ->
                        canvasSize = Offset(size.width.toFloat(), size.height.toFloat())
                    }
            ) {
                uiState.canvasItems.forEachIndexed { index, canvasItem ->
                    var scale by remember(index) { mutableFloatStateOf(canvasItem.scale) }
                    var offsetX by remember(index) { mutableFloatStateOf(canvasItem.x * canvasSize.x) }
                    var offsetY by remember(index) { mutableFloatStateOf(canvasItem.y * canvasSize.y) }

                    Box(
                        modifier = Modifier
                            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                            .size((100 * scale).dp)
                            .pointerInput(index) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(0.3f, 3f)
                                    val itemSize = 100 * scale
                                    offsetX = (offsetX + pan.x).coerceIn(0f, canvasSize.x - itemSize)
                                    offsetY = (offsetY + pan.y).coerceIn(0f, canvasSize.y - itemSize)
                                    viewModel.updateItemPosition(
                                        index,
                                        offsetX / canvasSize.x,
                                        offsetY / canvasSize.y
                                    )
                                    viewModel.updateItemScale(index, scale)
                                }
                            }
                    ) {
                        AsyncImage(
                            model = canvasItem.clothingItem.imageUrl,
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.fillMaxSize()
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.TopEnd)
                                .background(ShugaiBluePrimary, RoundedCornerShape(10.dp))
                                .clickable { viewModel.removeItemFromCanvas(index) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    text = "Добавить вещь",
                    fontFamily = InterFont,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = ShugaiPlaceholder
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(uiState.availableItems) { item ->
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.categoryName,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF5F5F5))
                                .clickable { viewModel.addItemToCanvas(item) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val selectedStyle = uiState.references?.styles
                    ?.find { it.id == uiState.selectedStyleId }

                Text(
                    text = "Стиль *",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.W500,
                    color = ShugaiPlaceholder,
                    fontFamily = InterFont
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ShugaiInputBg)
                        .clickable { showStylePicker = true }
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedStyle?.name ?: "Выбрать стиль",
                        fontSize = 14.sp,
                        fontFamily = InterFont,
                        color = if (selectedStyle != null) ShugaiTextPrimary else ShugaiPlaceholder,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = ShugaiPlaceholder,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (uiState.error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.error!!,
                        color = ShugaiError,
                        fontSize = 13.sp,
                        fontFamily = InterFont
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.saveWithCapture(canvasSize) },
                enabled = !uiState.isLoading && uiState.canvasItems.isNotEmpty(),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ShugaiBluePrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .height(51.dp)
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Сохранить наряд",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }

    if (showStylePicker && uiState.references != null) {
        val items = uiState.references!!.styles.map {
            PickerItem(id = it.id, name = it.name)
        }
        ItemFieldPickerScreen(
            title = "Стиль",
            items = items,
            selectedIds = setOfNotNull(uiState.selectedStyleId.ifBlank { null }),
            multiSelect = false,
            isLoading = false,
            onBack = { showStylePicker = false },
            onApply = { ids ->
                ids.firstOrNull()?.let { viewModel.onStyleChange(it) }
                showStylePicker = false
            }
        )
    }
}