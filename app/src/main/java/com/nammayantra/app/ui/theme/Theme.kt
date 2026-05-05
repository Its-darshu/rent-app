package com.nammayantra.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TractorGreen,
    onPrimary = Color.White,
    primaryContainer = TractorGreenDark,
    onPrimaryContainer = Color.White,
    secondary = EarthBrown,
    onSecondary = Color.White,
    secondaryContainer = FieldTan,
    onSecondaryContainer = EarthBrownDark,
    tertiary = MudYellow,
    onTertiary = DarkAsphalt,
    tertiaryContainer = MudYellow,
    onTertiaryContainer = EarthBrownDark,
    background = LightBackground,
    surface = Color.White,
    onSurface = DarkAsphalt,
    surfaceVariant = FieldTan,
    onSurfaceVariant = DarkAsphalt
)

@Composable
fun NammaYantraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // Force light rugged theme for showcase

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primaryContainer.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}