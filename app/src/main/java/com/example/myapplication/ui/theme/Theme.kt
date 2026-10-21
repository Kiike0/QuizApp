package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PistachioPrimary,
    onPrimary = Color.White,
    primaryContainer = PistachioContainer,
    onPrimaryContainer = OnPistachioContainer,
    secondary = NavyNavy,
    onSecondary = Color.White,
    background = BackgroundWhite,
    surface = SurfaceCard,
    onBackground = OnSurfaceText,
    onSurface = OnSurfaceText,
    outline = Color(0xFFE2E8F0)
)

private val DarkColorScheme = darkColorScheme(
    primary = PistachioLight,
    onPrimary = OnPistachioContainer,
    primaryContainer = PistachioDark,
    onPrimaryContainer = Color.White,
    secondary = PistachioPrimary,
    onSecondary = Color.White,
    background = Color(0xFF1B2E0B),
    surface = Color(0xFF243B10),
    onBackground = Color(0xFFF7FAF4),
    onSurface = Color(0xFFF7FAF4),
    outline = Color(0xFF3A591F)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
