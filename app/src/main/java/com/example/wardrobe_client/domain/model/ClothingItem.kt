package com.example.wardrobe_client.domain.model

data class ClothingItem(
    val id: String,
    val imageUrl: String,
    val categoryId: String,
    val categoryName: String,
    val categoryGroupName: String,
    val seasonIds: List<String>,
    val seasonNames: List<String>,
    val colorId: String,
    val colorName: String,
    val materialId: String,
    val materialName: String,
    val labels: List<Label>,
    val storagePlace: String,
    val comment: String
)