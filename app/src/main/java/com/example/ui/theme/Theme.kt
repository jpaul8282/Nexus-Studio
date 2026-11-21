package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = NexusBlue,
    onPrimary = Color.White,
    primaryContainer = NexusBlueContainer,
    onPrimaryContainer = NexusOnBlueContainer,
    secondary = Color(0xFF565E71),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFDAE2F9),
    onSecondaryContainer = Color(0xFF131C2B),
    tertiary = NexusOrange,
    onTertiary = Color.White,
    tertiaryContainer = NexusOrangeBg,
    onTertiaryContainer = Color(0xFF3B0900),
    background = NexusBg,
    onBackground = NexusTextPrimary,
    surface = NexusSurface,
    onSurface = NexusTextPrimary,
    surfaceVariant = NexusSurfaceVariant,
    onSurfaceVariant = NexusTextSecondary,
    outline = NexusBorder,
    outlineVariant = NexusBorderStrong
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFABC7FF),
    onPrimary = Color(0xFF002F68),
    primaryContainer = Color(0xFF004494),
    onPrimaryContainer = NexusBlueContainer,
    secondary = Color(0xFFBFC6DC),
    onSecondary = Color(0xFF283041),
    secondaryContainer = Color(0xFF3F4759),
    onSecondaryContainer = Color(0xFFDBE2F9),
    tertiary = Color(0xFFFFB59B),
    onTertiary = Color(0xFF5E1700),
    background = NexusDarkThemeBg,
    onBackground = NexusDarkThemeTextPrimary,
    surface = NexusDarkThemeSurface,
    onSurface = NexusDarkThemeTextPrimary,
    surfaceVariant = NexusDarkThemeSurfaceVariant,
    onSurfaceVariant = NexusDarkThemeTextSecondary,
    outline = NexusDarkThemeBorder,
    outlineVariant = Color(0xFF44474E)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Default to Clean Minimalism light theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
