package com.example.wardrobe_client.presentation.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Onboarding : Screen("onboarding")

    object Clothing : Screen("clothing")
    object ClothingItemDetail : Screen("clothing_item/{itemId}") {
        fun createRoute(itemId: String) = "clothing_item/$itemId"
    }
    object AddClothingItem : Screen("add_clothing_item")
    object EditClothingItem : Screen("edit_clothing_item/{itemId}") {
        fun createRoute(itemId: String) = "edit_clothing_item/$itemId"
    }
    object SwipeCompatibility : Screen("swipe_compatibility/{itemId}") {
        fun createRoute(itemId: String) = "swipe_compatibility/$itemId"
    }
    object ItemCategoryPicker : Screen("clothing_item_category/{itemId}") {
        fun createRoute(itemId: String) = "clothing_item_category/$itemId"
    }
    object ItemColorPicker : Screen("clothing_item_color/{itemId}") {
        fun createRoute(itemId: String) = "clothing_item_color/$itemId"
    }
    object ItemMaterialPicker : Screen("clothing_item_material/{itemId}") {
        fun createRoute(itemId: String) = "clothing_item_material/$itemId"
    }
    object ItemLabelsPicker : Screen("clothing_item_labels/{itemId}") {
        fun createRoute(itemId: String) = "clothing_item_labels/$itemId"
    }
    object Outfits : Screen("outfits")
    object OutfitDetail : Screen("outfit/{outfitId}") {
        fun createRoute(outfitId: String) = "outfit/$outfitId"
    }
    object OutfitEditor : Screen("outfit_editor?outfitId={outfitId}") {
        fun createRoute(outfitId: String? = null) =
            if (outfitId != null) "outfit_editor?outfitId=$outfitId" else "outfit_editor"
    }

    object VisualSearch : Screen("visual_search")

    object Trips : Screen("trips")
    object TripDetail : Screen("trip/{tripId}") {
        fun createRoute(tripId: String) = "trip/$tripId"
    }
    object TripItemsPicker : Screen("trip_items_picker/{tripId}") {
        fun createRoute(tripId: String) = "trip_items_picker/$tripId"
    }

    object AddTrip : Screen("add_trip")

    object Profile : Screen("profile")
}