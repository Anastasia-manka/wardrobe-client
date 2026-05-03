package com.example.wardrobe_client.domain.model

data class TemplateItem(
    val id: String,
    val imageUrl: String,
    val categoryId: String,
    val categoryName: String,
    val seasonId: String,
    val colorId: String,
    val colorName: String,
    val materialId: String
)