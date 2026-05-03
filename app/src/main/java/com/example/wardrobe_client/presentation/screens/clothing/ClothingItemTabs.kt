package com.example.wardrobe_client.presentation.screens.clothing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.wardrobe_client.domain.model.ClothingItem
import com.example.wardrobe_client.domain.model.Outfit

@Composable
fun ClothingItemDetailsTab(item: ClothingItem) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Категория: ${item.categoryGroupName} > ${item.categoryName}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Сезон: ${item.seasonNames.joinToString(", ")}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Цвет: ${item.colorName}")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Материал: ${item.materialName}")
        Spacer(modifier = Modifier.height(8.dp))
        if (item.labels.isNotEmpty()) {
            Text("Метки: ${item.labels.joinToString(", ") { it.name }}")
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (item.storagePlace.isNotEmpty()) {
            Text("Место хранения: ${item.storagePlace}")
            Spacer(modifier = Modifier.height(8.dp))
        }
        if (item.comment.isNotEmpty()) {
            Text("Комментарий: ${item.comment}")
        }
    }
}

@Composable
fun ClothingItemOutfitsTab(
    outfits: List<Outfit>,
    onOutfitClick: (String) -> Unit
) {
    if (outfits.isEmpty()) {
        Text(
            text = "Вещь не используется в нарядах",
            modifier = Modifier.padding(16.dp)
        )
        return
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(outfits) { outfit ->
            AsyncImage(
                model = outfit.coverUrl,
                contentDescription = outfit.styleName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onOutfitClick(outfit.id) }
            )
        }
    }
}

@Composable
fun ClothingItemCompatibleTab(
    compatibleItems: List<ClothingItem>,
    onSwipeClick: () -> Unit,
    onItemClick: (String) -> Unit
) {
    Column {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(compatibleItems) { item ->
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.categoryName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onItemClick(item.id) }
                )
            }
        }
        Button(
            onClick = onSwipeClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Подобрать одежду")
        }
    }
}