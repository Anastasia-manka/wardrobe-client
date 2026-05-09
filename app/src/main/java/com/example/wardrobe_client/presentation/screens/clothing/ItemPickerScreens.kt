package com.example.wardrobe_client.presentation.screens.clothing

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.wardrobe_client.presentation.navigation.Screen

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ItemCategoryPickerScreen(
    navController: NavController
) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry(Screen.ClothingItemDetail.route)
    }
    val viewModel: ClothingItemDetailViewModel = hiltViewModel(parentEntry)
    val uiState by viewModel.uiState.collectAsState()
    val refs = uiState.references
    val item = uiState.item

    val pickerItems = refs?.categoryGroups?.flatMap { group ->
        group.categories.map { cat ->
            PickerItem(id = cat.id, name = cat.name, groupName = group.name)
        }
    } ?: emptyList()

    val currentId = if (uiState.hasChanges) uiState.editedCategoryId else item?.categoryId
    val selectedIds = setOfNotNull(currentId)

    ItemFieldPickerScreen(
        title = "Категория",
        items = pickerItems,
        selectedIds = selectedIds,
        multiSelect = false,
        isLoading = uiState.isLoading,
        onBack = { navController.popBackStack() },
        onApply = { ids ->
            ids.firstOrNull()?.let { viewModel.onCategoryChange(it) }
            navController.popBackStack()
        }
    )
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ItemColorPickerScreen(
    navController: NavController
) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry(Screen.ClothingItemDetail.route)
    }
    val viewModel: ClothingItemDetailViewModel = hiltViewModel(parentEntry)
    val uiState by viewModel.uiState.collectAsState()
    val refs = uiState.references
    val item = uiState.item

    val pickerItems = refs?.colors?.map { color ->
        PickerItem(id = color.id, name = color.name)
    } ?: emptyList()

    val currentId = if (uiState.hasChanges) uiState.editedColorId else item?.colorId
    val selectedIds = setOfNotNull(currentId)

    ItemFieldPickerScreen(
        title = "Цвета",
        items = pickerItems,
        selectedIds = selectedIds,
        multiSelect = false,
        isLoading = uiState.isLoading,
        onBack = { navController.popBackStack() },
        onApply = { ids ->
            ids.firstOrNull()?.let { viewModel.onColorChange(it) }
            navController.popBackStack()
        }
    )
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ItemMaterialPickerScreen(
    navController: NavController
) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry(Screen.ClothingItemDetail.route)
    }
    val viewModel: ClothingItemDetailViewModel = hiltViewModel(parentEntry)
    val uiState by viewModel.uiState.collectAsState()
    val refs = uiState.references
    val item = uiState.item

    val pickerItems = refs?.materials?.map { material ->
        PickerItem(id = material.id, name = material.name)
    } ?: emptyList()

    val currentId = if (uiState.hasChanges) uiState.editedMaterialId else item?.materialId
    val selectedIds = setOfNotNull(currentId)

    ItemFieldPickerScreen(
        title = "Материал",
        items = pickerItems,
        selectedIds = selectedIds,
        multiSelect = false,
        isLoading = uiState.isLoading,
        onBack = { navController.popBackStack() },
        onApply = { ids ->
            ids.firstOrNull()?.let { viewModel.onMaterialChange(it) }
            navController.popBackStack()
        }
    )
}

@SuppressLint("UnrememberedGetBackStackEntry")
@Composable
fun ItemLabelsPickerScreen(
    navController: NavController
) {
    val parentEntry = remember(navController) {
        navController.getBackStackEntry(Screen.ClothingItemDetail.route)
    }
    val viewModel: ClothingItemDetailViewModel = hiltViewModel(parentEntry)
    val uiState by viewModel.uiState.collectAsState()
    val refs = uiState.references
    val item = uiState.item

    val pickerItems = refs?.labels?.map { label ->
        PickerItem(id = label.id, name = label.name)
    } ?: emptyList()

    val currentLabelIds = if (uiState.hasChanges) uiState.editedLabelIds.toSet()
    else item?.labels?.map { it.id }?.toSet() ?: emptySet()

    ItemFieldPickerScreen(
        title = "Метки",
        items = pickerItems,
        selectedIds = currentLabelIds,
        multiSelect = true,
        isLoading = uiState.isLoading,
        onBack = { navController.popBackStack() },
        onApply = { ids ->
            viewModel.updateLabels(ids.toList())
            navController.popBackStack()
        }
    )
}