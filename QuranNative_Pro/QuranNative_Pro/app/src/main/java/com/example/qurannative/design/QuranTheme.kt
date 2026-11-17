package com.example.qurannative.design

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ألوان التطبيق الأساسية
private val QuranGreen = Color(0xFF123C30)
private val QuranDarkGreen = Color(0xFF071A15)
private val QuranGold = Color(0xFFD9B66F)
private val QuranCream = Color(0xFFF8F5EC)
private val QuranText = Color(0xFF24352D)

private val DarkColors = darkColorScheme(
    primary = QuranGold,
    onPrimary = QuranDarkGreen,
    secondary = Color(0xFF8EB8A2),
    onSecondary = QuranDarkGreen,
    background = QuranDarkGreen,
    onBackground = QuranCream,
    surface = Color(0xFF102820),
    onSurface = QuranCream
)

private val LightColors = lightColorScheme(
    primary = QuranGreen,
    onPrimary = Color.White,
    secondary = Color(0xFF9A783B),
    onSecondary = Color.White,
    background = QuranCream,
    onBackground = QuranText,
    surface = Color.White,
    onSurface = QuranText
)

@Composable
fun QuranTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = MaterialTheme.typography,
        content = content
    )
}
