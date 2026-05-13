package com.example.wardrobe_client.presentation.screens.clothing

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import com.example.wardrobe_client.R
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.wardrobe_client.presentation.theme.*

@Composable
fun PhotoPickerBlock(
    uri: Uri?,
    onGallery: () -> Unit,
    onCamera: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, Color(0xFFD9D9D9), RoundedCornerShape(16.dp))
            .background(ShugaiSurface),
        contentAlignment = Alignment.Center
    ) {
        if (uri != null) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ItemFormPhotoButton(label = stringResource(R.string.clothing_photo_camera), onClick = onCamera)
                ItemFormPhotoButton(label = stringResource(R.string.clothing_photo_gallery), onClick = onGallery)
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddAPhoto,
                    contentDescription = null,
                    tint = ShugaiPlaceholder,
                    modifier = Modifier.size(36.dp)
                )
                Text(
                    text = stringResource(R.string.clothing_photo_upload),
                    fontSize = 13.sp,
                    color = ShugaiPlaceholder,
                    fontFamily = InterFont
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ItemFormPhotoButton(label = stringResource(R.string.clothing_photo_camera), onClick = onCamera)
                    ItemFormPhotoButton(label = stringResource(R.string.clothing_photo_gallery), onClick = onGallery)
                }
            }
        }
    }
}

@Composable
fun EditPhotoBlock(
    newUri: Uri?,
    existingUrl: String,
    onGallery: () -> Unit,
    onCamera: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(1.5.dp, Color(0xFFD9D9D9), RoundedCornerShape(16.dp))
            .background(ShugaiSurface),
        contentAlignment = Alignment.Center
    ) {
        val imageModel: Any? = newUri ?: existingUrl.ifBlank { null }

        if (imageModel != null) {
            AsyncImage(
                model = imageModel,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = null,
                tint = ShugaiPlaceholder,
                modifier = Modifier.size(36.dp)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ItemFormPhotoButton(label = stringResource(R.string.clothing_photo_camera), onClick = onCamera)
            ItemFormPhotoButton(label = stringResource(R.string.clothing_photo_gallery), onClick = onGallery)
        }
    }
}

@Composable
fun ItemFormPhotoButton(label: String, onClick: () -> Unit) {
    val icon = if (label == "Камера") Icons.Default.CameraAlt else Icons.Default.Photo
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .border(0.5.dp, Color(0xFFD9D9D9), RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = ShugaiTextSecondary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            fontFamily = InterFont,
            color = ShugaiTextSecondary
        )
    }
}

@Composable
fun AddItemSelectorField(
    label: String,
    value: String?,
    placeholder: String,
    required: Boolean = false,
    onClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = if (required) "$label *" else label,
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            color = ShugaiPlaceholder,
            fontFamily = InterFont
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(ShugaiInputBg)
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = value ?: placeholder,
                fontSize = 14.sp,
                fontFamily = InterFont,
                color = if (value != null) ShugaiTextPrimary else ShugaiPlaceholder,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = null,
                tint = ShugaiPlaceholder,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun AddItemTextField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            color = ShugaiPlaceholder,
            fontFamily = InterFont
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            minLines = minLines,
            textStyle = TextStyle(
                fontFamily = InterFont,
                fontSize = 14.sp,
                color = ShugaiTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(ShugaiInputBg)
                .padding(horizontal = 14.dp, vertical = 14.dp),
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        fontFamily = InterFont,
                        fontSize = 14.sp,
                        color = ShugaiPlaceholder
                    )
                }
                innerTextField()
            }
        )
    }
}