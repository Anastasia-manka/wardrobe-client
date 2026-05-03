package com.example.wardrobe_client.domain.model

data class ReferenceItem(
    val id: String,
    val name: String
)

data class CategoryGroup(
    val id: String,
    val name: String,
    val categories: List<ReferenceItem>
)

data class References(
    val categoryGroups: List<CategoryGroup>,
    val seasons: List<ReferenceItem>,
    val colors: List<ReferenceItem>,
    val materials: List<ReferenceItem>,
    val labels: List<ReferenceItem>,
    val styles: List<ReferenceItem>,
    val tripTypes: List<ReferenceItem>,
    val climates: List<ReferenceItem>,
    val activities: List<ReferenceItem>,
    val luggageTypes: List<ReferenceItem>
)