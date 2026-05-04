package com.example.wardrobe_client.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.wardrobe_client.R

val YauzaFont = FontFamily(
    Font(R.font.yauzatygra)
)

val InterFont = FontFamily(
    Font(R.font.inter)
)

val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = YauzaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        letterSpacing = 4.sp,
        color = ShugaiBluePrimary
    ),
    headlineLarge = TextStyle(
        fontFamily = YauzaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        letterSpacing = 3.sp,
        color = ShugaiBluePrimary
    ),
    headlineMedium = TextStyle(
        fontFamily = YauzaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        letterSpacing = 2.sp,
        color = ShugaiBluePrimary
    ),
    titleLarge = TextStyle(
        fontFamily = YauzaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        letterSpacing = 2.sp,
        color = ShugaiBluePrimary
    ),
    bodyLarge = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        color = ShugaiTextPrimary
    ),
    bodyMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = ShugaiTextSecondary
    ),
    bodySmall = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = ShugaiTextSecondary
    ),
    labelLarge = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = ShugaiTextOnPrimary
    ),
    labelMedium = TextStyle(
        fontFamily = InterFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = ShugaiTextSecondary
    )
)