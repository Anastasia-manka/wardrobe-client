package com.example.wardrobe_client.domain.model

data class Outfit(
    val id: String,
    val coverUrl: String,
    val styleId: String,
    val styleName: String,
    val items: List<OutfitItem>
)

data class OutfitItem(
    val itemId: String,
    val x: Float,
    val y: Float,
    val scale: Float
)