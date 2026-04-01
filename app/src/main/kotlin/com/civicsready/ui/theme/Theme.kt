package com.civicsready.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary          = Navy,
    onPrimary        = White,
    primaryContainer = LightBlue,
    onPrimaryContainer = Navy,
    secondary        = Red,
    onSecondary      = White,
    tertiary         = GreenPass,
    onTertiary       = White,
    tertiaryContainer = Amber,
    onTertiaryContainer = White,
    background       = OffWhite,
    onBackground     = Navy,
    surface          = White,
    onSurface        = Navy,
    surfaceVariant   = LightBlue,
    onSurfaceVariant = NavyLight,
    error            = RedFail,
    onError          = White
)

private val DarkColors = darkColorScheme(
    primary          = DarkNavy,
    onPrimary        = Navy,
    primaryContainer = DarkLightBlue,
    onPrimaryContainer = DarkNavy,
    secondary        = RedLight,
    onSecondary      = White,
    tertiary         = DarkGreenPass,
    onTertiary       = White,
    tertiaryContainer = DarkAmber,
    onTertiaryContainer = DarkBackground,
    background       = DarkBackground,
    onBackground     = White,
    surface          = DarkSurface,
    onSurface        = White,
    surfaceVariant   = DarkSurfaceVar,
    onSurfaceVariant = DarkNavyLight,
    error            = DarkRedFail,
    onError          = White
)

@Composable
fun CivicsReadyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AppTypography,
        content     = content
    )
}
