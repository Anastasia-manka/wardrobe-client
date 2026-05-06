package com.example.wardrobe_client.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = true
                isAppearanceLightNavigationBars = true
            }
        }
    }

    MaterialTheme(
        colorScheme = ShugaiColorScheme,
        typography = AppTypography,
        content = content
    )
}