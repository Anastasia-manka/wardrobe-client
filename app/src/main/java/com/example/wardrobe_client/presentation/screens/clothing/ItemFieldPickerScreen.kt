package com.example.wardrobe_client.presentation.screens.clothing

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wardrobe_client.presentation.theme.InterFont
import com.example.wardrobe_client.presentation.theme.ShugaiBluePrimary
import com.example.wardrobe_client.presentation.theme.ShugaiPlaceholder
import com.example.wardrobe_client.presentation.theme.ShugaiScreenBackground
import com.example.wardrobe_client.presentation.theme.YauzaFont
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.border

data class PickerItem(
    val id: String,
    val name: String,
    val groupName: String? = null
)

@Composable
fun ItemFieldPickerScreen(
    title: String,
    items: List<PickerItem>,
    selectedIds: Set<String>,
    multiSelect: Boolean,
    isLoading: Boolean,
    onBack: () -> Unit,
    onApply: (Set<String>) -> Unit
) {
    var currentSelection by remember(selectedIds) { mutableStateOf(selectedIds) }

    val grouped = items.groupBy { it.groupName }

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
                        .clickable { onBack() }
                )
                Text(
                    text = title,
                    fontFamily = YauzaFont,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W400,
                    color = ShugaiBluePrimary
                )
                Spacer(modifier = Modifier.size(24.dp))
            }

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = ShugaiBluePrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp)
                ) {
                    grouped.forEach { (group, groupItems) ->
                        if (group != null) {
                            item {
                                Text(
                                    text = group,
                                    fontFamily = YauzaFont,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.W400,
                                    color = ShugaiPlaceholder,
                                    modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
                                )
                            }
                        }
                        items(groupItems) { pickerItem ->
                            PickerRow(
                                item = pickerItem,
                                isSelected = currentSelection.contains(pickerItem.id),
                                onClick = {
                                    currentSelection = if (multiSelect) {
                                        if (currentSelection.contains(pickerItem.id)) {
                                            currentSelection - pickerItem.id
                                        } else {
                                            currentSelection + pickerItem.id
                                        }
                                    } else {
                                        setOf(pickerItem.id)
                                    }
                                }
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }

                Button(
                    onClick = { onApply(currentSelection) },
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
                    Text(
                        text = "Применить",
                        fontFamily = InterFont,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                }
            }
        }
    }
}

@Composable
private fun PickerRow(
    item: PickerItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable { onClick() },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.name,
                fontFamily = InterFont,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500,
                color = Color.Black
            )
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = if (isSelected) ShugaiBluePrimary else Color.Transparent,
                        shape = CircleShape
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (isSelected) ShugaiBluePrimary else Color(0xFFD9D9D9),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(Color(0xFFD9D9D9))
        )
    }
}