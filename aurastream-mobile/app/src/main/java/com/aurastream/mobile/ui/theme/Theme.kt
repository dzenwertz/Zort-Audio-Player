package com.aurastream.mobile.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = SpotifyGreen,
    secondary = SpotifyGreenDark,
    background = SpotifyDark,
    surface = SpotifySurface,
    surfaceVariant = SpotifySurfaceVariant,
    onPrimary = SpotifyDark,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = DangerRed
)

@Composable
fun AuraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
