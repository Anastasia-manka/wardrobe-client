package com.example.wardrobe_client.presentation.screens.clothing

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiScreenBackground
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border

private data class CategoryFilter(val label: String, val groupId: String?)

private val categoryFilters = listOf(
    CategoryFilter("Все", null),
    CategoryFilter("Верхняя одежда", "Верхняя одежда"),
    CategoryFilter("Верх", "Верх"),
    CategoryFilter("Низ", "Низ"),
    CategoryFilter("Платья", "Платье"),
    CategoryFilter("Костюмы", "Костюмы и комплекты"),
    CategoryFilter("Обувь", "Обувь"),
    CategoryFilter("Аксессуары", "Аксессуары"),
    CategoryFilter("Пляжная", "Пляжная одежда"),
)

@Composable
fun SwipeCompatibilityScreen(
    navController: NavController,
    viewModel: SwipeCompatibilityViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ShugaiScreenBackground)
            .statusBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = ShugaiBluePrimary,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterStart)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "С чем носить",
                fontFamily = InterFont,
                fontSize = 18.sp,
                fontWeight = FontWeight.W600,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        uiState.sourceItem?.let { source ->
            AsyncImage(
                model = source.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFD9D9D9))
            )
        } ?: Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFFD9D9D9))
        )

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFFD9D9D9)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = ShugaiBluePrimary)
                }
            }

            uiState.isEmpty -> {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(30.dp))
                        .background(Color(0xFFD9D9D9)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Вещей для подбора нет",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            else -> {
                uiState.currentItem?.let { item ->
                    SwipeCard(
                        imageUrl = item.imageUrl,
                        onSwipeRight = viewModel::swipeRight,
                        onSwipeLeft = viewModel::swipeLeft
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categoryFilters) { filter ->

                val isSelected = uiState.selectedCategoryGroupName == filter.groupId
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            if (isSelected) ShugaiBluePrimary else Color.Transparent
                        )
                        .border(
                            width = 1.dp,
                            color = if (isSelected) ShugaiBluePrimary else Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clickable { viewModel.filterByGroup(filter.groupId) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = filter.label,
                        fontFamily = InterFont,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun SwipeCard(
    imageUrl: String,
    onSwipeRight: () -> Unit,
    onSwipeLeft: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    var dragOffset by remember { mutableFloatStateOf(0f) }

    val rotation = (offsetX.value / 20f).coerceIn(-10f, 10f)
    val greenAlpha = (offsetX.value / 400f).coerceIn(0f, 1f)
    val redAlpha = (-offsetX.value / 400f).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .rotate(rotation)
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFFD9D9D9))
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                when {
                                    dragOffset > 300f -> {
                                        offsetX.animateTo(
                                            1000f,
                                            animationSpec = spring()
                                        )
                                        onSwipeRight()
                                        offsetX.snapTo(0f)
                                    }
                                    dragOffset < -300f -> {
                                        offsetX.animateTo(
                                            -1000f,
                                            animationSpec = spring()
                                        )
                                        onSwipeLeft()
                                        offsetX.snapTo(0f)
                                    }
                                    else -> {
                                        offsetX.animateTo(
                                            0f,
                                            animationSpec = spring(
                                                dampingRatio = 0.6f,
                                                stiffness = 300f
                                            )
                                        )
                                    }
                                }
                                dragOffset = 0f
                            }
                        },
                        onHorizontalDrag = { _, amount ->
                            dragOffset += amount
                            scope.launch {
                                offsetX.snapTo(offsetX.value + amount)
                            }
                        }
                    )
                }
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(30.dp))
            )

            if (greenAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color(0xFF4CAF50).copy(alpha = greenAlpha * 0.5f),
                            RoundedCornerShape(30.dp)
                        )
                )
            }

            if (redAlpha > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Color(0xFFC33636).copy(alpha = redAlpha * 0.5f),
                            RoundedCornerShape(30.dp)
                        )
                )
            }
        }
    }
}