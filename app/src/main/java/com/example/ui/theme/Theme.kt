package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ElectricVioletLight,
    onPrimary = Color.White,
    primaryContainer = ElectricVioletDark,
    onPrimaryContainer = Color.White,
    secondary = EmeraldLowestPrice,
    onSecondary = Color.Black,
    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = Color(0xFF3B3654)
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricViolet,
    onPrimary = Color.White,
    primaryContainer = ElectricVioletContainer,
    onPrimaryContainer = OnElectricVioletContainer,
    secondary = EmeraldLowestPrice,
    onSecondary = Color.White,
    background = FrostLavenderCanvas,
    onBackground = InkTextPrimary,
    surface = FrostLavenderCard,
    onSurface = InkTextPrimary,
    surfaceVariant = FrostLavenderSurfaceVariant,
    onSurfaceVariant = InkTextSecondary,
    outline = FrostLavenderBorder
)

@Composable
fun NammaRigTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
