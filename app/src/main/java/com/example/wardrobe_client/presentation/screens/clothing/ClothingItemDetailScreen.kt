package com.example.wardrobe_client.presentation.screens.clothing


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobe_client.R
import com.example.wardrobe_client.presentation.navigation.Screen
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiScreenBackground
import com.example.wardrobe_client.presentation.theme.YauzaFont
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.border
import com.example.wardrobe_client.domain.model.ReferenceItem
import com.example.wardrobe_client.presentation.theme.ShugaiPlaceholder
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle


@Composable
fun ClothingItemDetailScreen(
    navController: NavController,
    viewModel: ClothingItemDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Удалить вещь?") },
            text = { Text("Вещь будет удалена из гардероба. Это действие необратимо.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.deleteItem()
                }) {
                    Text("Удалить", color = Color(0xFFC33636))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(
                        "Отмена",
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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

                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            showMenu = false
                            navController.navigate(Screen.EditClothingItem.createRoute(uiState.item?.id ?: ""))
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
                            text = "Изменить изображение",
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
                            text = "Удалить позицию",
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

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = ShugaiBluePrimary)
        }
        return
    }

    val item = uiState.item ?: return
    val tabs = listOf("ДЕТАЛИ", "НАРЯДЫ", "С ЧЕМ НОСИТЬ")

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
                model = item.imageUrl,
                contentDescription = item.categoryName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .height(300.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xFFD9D9D9))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                tabs.forEachIndexed { index, tab ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { selectedTab = index }
                    ) {
                        Text(
                            text = tab,
                            fontFamily = YauzaFont,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W400,
                            color = ShugaiBluePrimary
                        )
                        if (selectedTab == index) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .height(2.dp)
                                    .width(tab.length.dp * 10)
                                    .background(ShugaiBluePrimary)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (selectedTab) {
                0 -> DetailsTab(
                    uiState = uiState,
                    viewModel = viewModel,
                    navController = navController
                )
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

        if (uiState.hasChanges) {
            Button(
                onClick = viewModel::saveChanges,
                enabled = !uiState.isSaving,
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ShugaiBluePrimary,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .fillMaxWidth()
                    .height(51.dp)
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        text = "Сохранить",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailsTab(
    uiState: ClothingItemDetailUiState,
    viewModel: ClothingItemDetailViewModel,
    navController: NavController
) {
    val item = uiState.item ?: return
    val refs = uiState.references

    val categoryName = refs?.categoryGroups
        ?.flatMap { it.categories }
        ?.find { it.id == (if (uiState.hasChanges) uiState.editedCategoryId else item.categoryId) }
        ?.name ?: item.categoryName

    val colorName = refs?.colors
        ?.find { it.id == (if (uiState.hasChanges) uiState.editedColorId else item.colorId) }
        ?.name ?: item.colorName

    val materialName = refs?.materials
        ?.find { it.id == (if (uiState.hasChanges) uiState.editedMaterialId else item.materialId) }
        ?.name ?: item.materialName

    val seasonNames = refs?.seasons
        ?.filter { season ->
            val ids = if (uiState.hasChanges) uiState.editedSeasonIds else item.seasonIds
            ids.contains(season.id)
        }
        ?.joinToString(", ") { it.name } ?: item.seasonNames.joinToString(", ")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        DetailRow(
            label = "Категория",
            value = categoryName,
            onClick = {
                viewModel.startEditing()
                navController.navigate("clothing_item_category/${item.id}")
            }
        )
        SeasonRow(
            seasons = refs?.seasons ?: emptyList(),
            selectedIds = if (uiState.hasChanges) uiState.editedSeasonIds else item.seasonIds,
            onToggle = { seasonId ->
                if (!uiState.hasChanges) viewModel.startEditing()
                viewModel.onSeasonSelect(seasonId)
            }
        )
        DetailRow(
            label = "Цвет",
            value = colorName,
            onClick = {
                viewModel.startEditing()
                navController.navigate("clothing_item_color/${item.id}")
            }
        )
        DetailRow(
            label = "Материал",
            value = materialName,
            onClick = {
                viewModel.startEditing()
                navController.navigate("clothing_item_material/${item.id}")
            }
        )
        val labelNames = if (uiState.hasChanges) {
            uiState.editedLabelIds.mapNotNull { id ->
                refs?.labels?.find { it.id == id }?.name
            }.joinToString(", ")
        } else {
            item.labels.joinToString(", ") { it.name }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    viewModel.startEditing()
                    navController.navigate(Screen.ItemLabelsPicker.createRoute(item.id))
                }
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Метки",
                    fontFamily = InterFont,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    color = ShugaiPlaceholder
                )
                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = null,
                    tint = ShugaiBluePrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
            if (labelNames.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    labelNames.split(", ").forEach { label ->
                        Box(
                            modifier = Modifier
                                .border(
                                    width = 1.dp,
                                    color = Color(0xFFD9D9D9),
                                    shape = RoundedCornerShape(30.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = label,
                                fontFamily = InterFont,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W500,
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Место хранения",
                fontFamily = InterFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = ShugaiPlaceholder
            )
            BasicTextField(
                value = if (uiState.hasChanges) uiState.editedStoragePlace else item.storagePlace,
                onValueChange = {
                    viewModel.startEditing()
                    viewModel.onStoragePlaceChange(it)
                },
                textStyle = TextStyle(
                    fontFamily = InterFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.Black
                ),
                cursorBrush = SolidColor(ShugaiBluePrimary),
                decorationBox = { innerTextField ->
                    Column {
                        Box(modifier = Modifier.padding(vertical = 8.dp)) {
                            if ((if (uiState.hasChanges) uiState.editedStoragePlace else item.storagePlace).isEmpty()) {
                                Text(
                                    text = "Например, на верхней полке",
                                    fontFamily = InterFont,
                                    fontSize = 16.sp,
                                    color = ShugaiPlaceholder
                                )
                            }
                            innerTextField()
                        }
                        Divider(color = Color(0xFFD9D9D9), thickness = 1.dp)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Комментарий",
                fontFamily = InterFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                color = ShugaiPlaceholder
            )
            BasicTextField(
                value = if (uiState.hasChanges) uiState.editedComment else item.comment,
                onValueChange = {
                    viewModel.startEditing()
                    viewModel.onCommentChange(it)
                },
                textStyle = TextStyle(
                    fontFamily = InterFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = Color.Black
                ),
                cursorBrush = SolidColor(ShugaiBluePrimary),
                decorationBox = { innerTextField ->
                    Column {
                        Box(modifier = Modifier.padding(vertical = 8.dp)) {
                            if ((if (uiState.hasChanges) uiState.editedComment else item.comment).isEmpty()) {
                                Text(
                                    text = "Например, хочу носить с яркими аксессуарами",
                                    fontFamily = InterFont,
                                    fontSize = 16.sp,
                                    color = ShugaiPlaceholder
                                )
                            }
                            innerTextField()
                        }
                        Divider(color = Color(0xFFD9D9D9), thickness = 1.dp)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            fontFamily = InterFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = ShugaiPlaceholder
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = Color(0xFFD9D9D9),
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = value.ifEmpty { "—" },
                    fontFamily = InterFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W500,
                    color = Color.Black
                )
            }
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = ShugaiBluePrimary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SeasonRow(
    seasons: List<ReferenceItem>,
    selectedIds: List<String>,
    onToggle: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Сезон",
            fontFamily = InterFont,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            color = ShugaiPlaceholder
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            seasons.forEach { season ->
                val isSelected = selectedIds.contains(season.id)
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = if (isSelected) ShugaiBluePrimary else Color(0xFFD9D9D9),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .background(
                            color = if (isSelected) ShugaiBluePrimary else Color.Transparent,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clickable { onToggle(season.id) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = season.name,
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