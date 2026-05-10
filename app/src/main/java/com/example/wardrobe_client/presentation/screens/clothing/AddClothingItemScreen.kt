package com.example.wardrobe_client.presentation.screens.clothing

import android.content.pm.PackageManager
import androidx.compose.foundation.text.BasicTextField
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.wardrobe_client.domain.model.ReferenceItem
import com.example.wardrobe_client.presentation.theme.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import java.util.jar.Manifest

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddClothingItemScreen(
    navController: NavController,
    viewModel: AddEditClothingItemViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val refs = uiState.references
    val context = LocalContext.current
    var pendingCameraLaunch by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) navController.popBackStack()
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? -> uri?.let { viewModel.onPhotoSelected(it) } }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success -> if (success) viewModel.onCameraPhotoTaken() }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted && pendingCameraLaunch) {
            pendingCameraLaunch = false
            val uri = viewModel.createCameraUri()
            cameraLauncher.launch(uri)
        }
    }

    var showCategoryPicker by remember { mutableStateOf(false) }
    var showSeasonPicker by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showMaterialPicker by remember { mutableStateOf(false) }
    var showLabelsPicker by remember { mutableStateOf(false) }


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
                    text = "Добавить вещь",
                    fontFamily = YauzaFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400,
                    color = ShugaiBluePrimary
                )
                Spacer(modifier = Modifier.size(24.dp))
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PhotoPickerBlock(
                    uri = uiState.photoUri,
                    onGallery = { galleryLauncher.launch("image/*") },
                    onCamera = {
                        val permission = android.Manifest.permission.CAMERA
                        val granted = androidx.core.content.ContextCompat.checkSelfPermission(
                            context, permission
                        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

                        if (granted) {
                            val uri = viewModel.createCameraUri()
                            cameraLauncher.launch(uri)
                        } else {
                            pendingCameraLaunch = true
                            permissionLauncher.launch(permission)
                        }
                    }
                )

                val selectedCategory = refs?.categoryGroups
                    ?.flatMap { it.categories }
                    ?.find { it.id == uiState.categoryId }

                AddItemSelectorField(
                    label = "Категория",
                    value = selectedCategory?.name,
                    placeholder = "Выбрать категорию",
                    required = true,
                    onClick = { showCategoryPicker = true }
                )

                val selectedSeason = refs?.seasons?.find { it.id == uiState.seasonId }

                AddItemSelectorField(
                    label = "Сезон",
                    value = selectedSeason?.name,
                    placeholder = "Выбрать сезон",
                    required = true,
                    onClick = { showSeasonPicker = true }
                )


                val selectedColor = refs?.colors?.find { it.id == uiState.colorId }

                AddItemSelectorField(
                    label = "Цвет",
                    value = selectedColor?.name,
                    placeholder = "Выбрать цвет",
                    required = true,
                    onClick = { showColorPicker = true }
                )

                val selectedMaterial = refs?.materials?.find { it.id == uiState.materialId }

                AddItemSelectorField(
                    label = "Материал",
                    value = selectedMaterial?.name,
                    placeholder = "Выбрать материал",
                    required = true,
                    onClick = { showMaterialPicker = true }
                )

                val selectedLabels = refs?.labels?.filter { it.id in uiState.labels.map { l -> l.id } }

                AddItemSelectorField(
                    label = "Метки",
                    value = selectedLabels?.takeIf { it.isNotEmpty() }?.joinToString(", ") { it.name },
                    placeholder = "Выбрать метки",
                    onClick = { showLabelsPicker = true }
                )

                AddItemTextField(
                    label = "Место хранения",
                    value = uiState.storagePlace,
                    placeholder = "Например: шкаф, 2-я полка",
                    onValueChange = viewModel::onStoragePlaceChange
                )

                AddItemTextField(
                    label = "Комментарий",
                    value = uiState.comment,
                    placeholder = "Заметки об уходе, размере...",
                    onValueChange = viewModel::onCommentChange,
                    minLines = 3
                )

                if (uiState.error != null) {
                    Text(
                        text = uiState.error!!,
                        color = ShugaiError,
                        fontSize = 13.sp,
                        fontFamily = InterFont
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = viewModel::save,
                enabled = !uiState.isLoading,
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
                        text = "Сохранить",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }

    if (showCategoryPicker && refs != null) {
        val items = refs.categoryGroups.flatMap { group ->
            group.categories.map { cat ->
                PickerItem(id = cat.id, name = cat.name, groupName = group.name)
            }
        }
        ItemFieldPickerScreen(
            title = "Категория",
            items = items,
            selectedIds = setOfNotNull(uiState.categoryId.ifBlank { null }),
            multiSelect = false,
            isLoading = false,
            onBack = { showCategoryPicker = false },
            onApply = { ids ->
                ids.firstOrNull()?.let { viewModel.onCategoryChange(it) }
                showCategoryPicker = false
            }
        )
    }

    if (showSeasonPicker && refs != null) {
        val items = refs.seasons.map { PickerItem(id = it.id, name = it.name) }
        ItemFieldPickerScreen(
            title = "Сезон",
            items = items,
            selectedIds = setOfNotNull(uiState.seasonId.ifBlank { null }),
            multiSelect = false,
            isLoading = false,
            onBack = { showSeasonPicker = false },
            onApply = { ids ->
                ids.firstOrNull()?.let { viewModel.onSeasonChange(it) }
                showSeasonPicker = false
            }
        )
    }

    if (showColorPicker && refs != null) {
        val items = refs.colors.map { PickerItem(id = it.id, name = it.name) }
        ItemFieldPickerScreen(
            title = "Цвет",
            items = items,
            selectedIds = setOfNotNull(uiState.colorId.ifBlank { null }),
            multiSelect = false,
            isLoading = false,
            onBack = { showColorPicker = false },
            onApply = { ids ->
                ids.firstOrNull()?.let { viewModel.onColorChange(it) }
                showColorPicker = false
            }
        )
    }

    if (showMaterialPicker && refs != null) {
        val items = refs.materials.map { PickerItem(id = it.id, name = it.name) }
        ItemFieldPickerScreen(
            title = "Материал",
            items = items,
            selectedIds = setOfNotNull(uiState.materialId.ifBlank { null }),
            multiSelect = false,
            isLoading = false,
            onBack = { showMaterialPicker = false },
            onApply = { ids ->
                ids.firstOrNull()?.let { viewModel.onMaterialChange(it) }
                showMaterialPicker = false
            }
        )
    }

    if (showLabelsPicker && refs != null) {
        val items = refs.labels.map { PickerItem(id = it.id, name = it.name) }
        val selectedLabelIds = uiState.labels.map { it.id }.toSet()
        ItemFieldPickerScreen(
            title = "Метки",
            items = items,
            selectedIds = selectedLabelIds,
            multiSelect = true,
            isLoading = false,
            onBack = { showLabelsPicker = false },
            onApply = { ids ->
                val selected = refs.labels.filter { it.id in ids }
                selected.forEach { ref ->
                    val asLabel = com.example.wardrobe_client.domain.model.Label(
                        id = ref.id,
                        name = ref.name,
                        isCustom = false
                    )
                    if (asLabel.id !in uiState.labels.map { it.id }) {
                        viewModel.onLabelToggle(asLabel)
                    }
                }
                uiState.labels.forEach { existing ->
                    if (existing.id !in ids) viewModel.onLabelToggle(existing)
                }
                showLabelsPicker = false
            }
        )
    }
}