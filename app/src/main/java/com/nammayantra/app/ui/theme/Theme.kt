package com.nammayantra.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KrishiColorScheme = lightColorScheme(
    primary = ForestGreen,
    onPrimary = OnForestGreen,
    primaryContainer = ForestGreenContainer,
    onPrimaryContainer = OnForestGreenContainer,
    secondary = EarthOrange,
    onSecondary = OnEarthOrange,
    secondaryContainer = EarthOrangeContainer,
    onSecondaryContainer = OnEarthOrange,
    background = OffWhite,
    surface = OffWhite,
    onSurface = InkBlack,
    onBackground = InkBlack,
    surfaceVariant = NammaSurfaceVariant,
    onSurfaceVariant = NammaOnSurfaceVariant,
    outline = NammaOutline,
    outlineVariant = NammaOutlineVariant,
    error = ErrorRed,
    onError = PureWhite
)

@Composable
fun NammaYantraTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = ForestGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }
    MaterialTheme(colorScheme = KrishiColorScheme, content = content)
}
