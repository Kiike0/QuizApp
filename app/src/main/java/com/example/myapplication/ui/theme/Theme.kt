package com.example.myapplication.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = NavyMedium,
    onPrimary = Color.White,
    primaryContainer = YellowContainer,
    onPrimaryContainer = NavyDeep,
    secondary = GoldAccent,
    onSecondary = Color.White,
    background = BackgroundSoftYellow,
    surface = SurfaceCard,
    onBackground = OnSurfaceText,
    onSurface = OnSurfaceText,
    outline = Color(0xFFE2E8F0)
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldAccent,
    onPrimary = NavyDeep,
    primaryContainer = NavyMedium,
    onPrimaryContainer = Color.White,
    secondary = NavyLight,
    onSecondary = Color.White,
    background = NavyDeep,
    surface = Color(0xFF132238),
    onBackground = Color(0xFFFFFBEB),
    onSurface = Color(0xFFFFFBEB),
    outline = Color(0xFF2A3E59)
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
