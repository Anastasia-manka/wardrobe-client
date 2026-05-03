package com.example.wardrobe_client.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checkroom
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val labelRu: String
) {
    object Clothing : BottomNavItem(
        route = Screen.Clothing.route,
        icon = Icons.Default.Checkroom,
        labelRu = "Одежда"
    )
    object Outfits : BottomNavItem(
        route = Screen.Outfits.route,
        icon = Icons.Default.Style,
        labelRu = "Наряды"
    )
    object VisualSearch : BottomNavItem(
        route = Screen.VisualSearch.route,
        icon = Icons.Default.Search,
        labelRu = "Поиск"
    )
    object Trips : BottomNavItem(
        route = Screen.Trips.route,
        icon = Icons.Default.Work,
        labelRu = "Поездки"
    )
    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        icon = Icons.Default.Person,
        labelRu = "Профиль"
    )
}