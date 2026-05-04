package com.example.wardrobe_client.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val ShugaiColorScheme = lightColorScheme(
    primary = ShugaiBluePrimary,
    onPrimary = ShugaiTextOnPrimary,
    primaryContainer = ShugaiBlueLight,
    onPrimaryContainer = ShugaiTextOnPrimary,
    secondary = ShugaiBlueLight,
    onSecondary = ShugaiTextOnPrimary,
    background = ShugaiBackground,
    onBackground = ShugaiTextPrimary,
    surface = ShugaiSurface,
    onSurface = ShugaiTextPrimary,
    surfaceVariant = ShugaiInputBackground,
    onSurfaceVariant = ShugaiTextSecondary,
    error = ShugaiError,
    onError = ShugaiTextOnPrimary
)

@Composable
fun ShugaiTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ShugaiColorScheme,
        typography = AppTypography,
        content = content
    )
}