package com.example.wardrobe_client.presentation.screens.outfits

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.theme.*

@Composable
fun OutfitDetailScreen(
    navController: NavController,
    viewModel: OutfitDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isDeleted) {
        if (uiState.isDeleted) navController.popBackStack()
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text("Удалить наряд?", fontFamily = InterFont, fontWeight = FontWeight.W500)
            },
            text = {
                Text(
                    "Наряд будет удалён. Это действие необратимо.",
                    fontFamily = InterFont
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteOutfit()
                }) {
                    Text("Удалить", color = Color(0xFFC33636), fontFamily = InterFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        "Отмена",
                        fontFamily = InterFont,
                        color = Color.Black
                    )
                }
            }
        )
    }

    if (showMenu) {
        Dialog(onDismissRequest = { showMenu = false }) {
            Box(
                modifier = Modifier
                    .width(322.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Выберите действие",
                            fontFamily = InterFont,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W500,
                            color = Color.Black,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = ShugaiBluePrimary,
                            modifier = Modifier
                                .size(24.dp)
                                .clickable { showMenu = false }
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            showMenu = false
                            navController.navigate(
                                Screen.OutfitEditor.createRoute(uiState.outfit?.id)
                            )
                        },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ShugaiBluePrimary,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Редактировать",
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500
                        )
                    }

                    Button(
                        onClick = {
                            showMenu = false
                            showDeleteDialog = true
                        },
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Color(0xFFC33636)
                        ),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFC33636)),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Удалить",
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color(0xFFC33636)
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ShugaiScreenBackground)
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                color = ShugaiBluePrimary,
                modifier = Modifier.align(Alignment.Center)
            )
            return@Box
        }

        val outfit = uiState.outfit ?: return@Box

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
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
                    text = "Наряд",
                    fontFamily = YauzaFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400,
                    color = ShugaiBluePrimary
                )
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = ShugaiBluePrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { showMenu = true }
                )
            }

            AsyncImage(
                model = outfit.coverUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                if (outfit.items.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Вещи",
                            fontFamily = YauzaFont,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W400,
                            color = Color.Black
                        )
                        Text(
                            text = "${outfit.items.size}",
                            fontFamily = InterFont,
                            fontSize = 14.sp,
                            color = ShugaiPlaceholder
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(end = 8.dp)
                    ) {
                        items(outfit.items) { outfitItem ->
                            AsyncImage(
                                model = outfitItem.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(90.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF5F5F5))
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (!outfit.styleName.isNullOrBlank()) {
                    Text(
                        text = "Стиль",
                        fontFamily = InterFont,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.W500,
                        color = ShugaiPlaceholder
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(30.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = outfit.styleName,
                            fontFamily = InterFont,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.W500,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}