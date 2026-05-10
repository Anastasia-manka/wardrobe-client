package com.example.wardrobe_client.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.wardrobe_client.presentation.screens.auth.LoginScreen
import com.example.wardrobe_client.presentation.screens.auth.OnboardingScreen
import com.example.wardrobe_client.presentation.screens.auth.RegisterScreen
import com.example.wardrobe_client.presentation.screens.clothing.AddClothingItemScreen
import com.example.wardrobe_client.presentation.screens.clothing.ClothingItemDetailScreen
import com.example.wardrobe_client.presentation.screens.clothing.ClothingScreen
import com.example.wardrobe_client.presentation.screens.clothing.EditClothingItemScreen
import com.example.wardrobe_client.presentation.screens.clothing.SwipeCompatibilityScreen
import com.example.wardrobe_client.presentation.screens.outfits.OutfitDetailScreen
import com.example.wardrobe_client.presentation.screens.outfits.OutfitEditorScreen
import com.example.wardrobe_client.presentation.screens.outfits.OutfitsScreen
import com.example.wardrobe_client.presentation.screens.profile.ProfileScreen
import com.example.wardrobe_client.presentation.screens.search.VisualSearchScreen
import com.example.wardrobe_client.presentation.screens.trips.AddTripScreen
import com.example.wardrobe_client.presentation.screens.trips.TripDetailScreen
import com.example.wardrobe_client.presentation.screens.trips.TripItemsPickerScreen
import com.example.wardrobe_client.presentation.screens.trips.TripsScreen
import com.example.wardrobe_client.presentation.screens.clothing.ItemCategoryPickerScreen
import com.example.wardrobe_client.presentation.screens.clothing.ItemColorPickerScreen
import com.example.wardrobe_client.presentation.screens.clothing.ItemLabelsPickerScreen
import com.example.wardrobe_client.presentation.screens.clothing.ItemMaterialPickerScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(Screen.Onboarding.route) {
            OnboardingScreen(navController = navController)
        }
        composable(Screen.Clothing.route) {
            ClothingScreen(navController = navController)
        }
        composable(
            route = Screen.ClothingItemDetail.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            ClothingItemDetailScreen(navController = navController)
        }
        composable(Screen.AddClothingItem.route) {
            AddClothingItemScreen(navController = navController)
        }
        composable(
            route = Screen.EditClothingItem.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            EditClothingItemScreen(navController = navController)
        }
        composable(
            route = Screen.SwipeCompatibility.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            SwipeCompatibilityScreen(navController = navController)
        }
        composable(
            route = Screen.ItemCategoryPicker.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            ItemCategoryPickerScreen(navController = navController)
        }
        composable(
            route = Screen.ItemColorPicker.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            ItemColorPickerScreen(navController = navController)
        }
        composable(
            route = Screen.ItemMaterialPicker.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            ItemMaterialPickerScreen(navController = navController)
        }
        composable(
            route = Screen.ItemLabelsPicker.route,
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) {
            ItemLabelsPickerScreen(navController = navController)
        }
        composable(Screen.Outfits.route) {
            OutfitsScreen(navController = navController)
        }
        composable(
            route = Screen.OutfitDetail.route,
            arguments = listOf(navArgument("outfitId") { type = NavType.StringType })
        ) {
            OutfitDetailScreen(navController = navController)
        }
        composable(
            route = Screen.OutfitEditor.route,
            arguments = listOf(navArgument("outfitId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            OutfitEditorScreen(navController = navController)
        }
        composable(Screen.VisualSearch.route) {
            VisualSearchScreen(navController = navController)
        }
        composable(Screen.Trips.route) {
            TripsScreen(navController = navController)
        }
        composable(
            route = Screen.TripDetail.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) {
            TripDetailScreen(navController = navController)
        }
        composable(Screen.AddTrip.route) {
            AddTripScreen(navController = navController)
        }
        composable(
            route = Screen.TripItemsPicker.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) {
            TripItemsPickerScreen(navController = navController)
        }
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}