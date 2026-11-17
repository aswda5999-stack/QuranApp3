package com.example.qurannative.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val QuranGreen = Color(0xFF145C45)
private val QuranGreenDark = Color(0xFF0B392D)
private val QuranGold = Color(0xFFC6A45B)

private val LightColors = lightColorScheme(
    primary = QuranGreen,
    onPrimary = Color.White,
    secondary = QuranGold,
    onSecondary = Color(0xFF211A0B),
    background = Color(0xFFF8F7F1),
    onBackground = Color(0xFF20251F),
    surface = Color(0xFFFFFEFA),
    onSurface = Color(0xFF20251F),
    surfaceVariant = Color(0xFFECEBE3),
    onSurfaceVariant = Color(0xFF62675F)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF8BC9A8),
    onPrimary = Color(0xFF063B2B),
    secondary = Color(0xFFE1C27D),
    onSecondary = Color(0xFF35280C),
    background = Color(0xFF101713),
    onBackground = Color(0xFFE7EEE8),
    surface = Color(0xFF17211B),
    onSurface = Color(0xFFE7EEE8),
    surfaceVariant = Color(0xFF28362E),
    onSurfaceVariant = Color(0xFFBAC8BD)
)

@Composable
fun QuranTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}
