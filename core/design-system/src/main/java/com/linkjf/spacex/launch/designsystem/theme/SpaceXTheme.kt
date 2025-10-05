package com.linkjf.spacex.launch.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
    darkColorScheme(
        primary = SpaceXColors.Primary,
        onPrimary = SpaceXColors.OnBackground,
        primaryContainer = SpaceXColors.PrimaryVariant,
        onPrimaryContainer = SpaceXColors.OnBackground,
        secondary = SpaceXColors.Interactive,
        onSecondary = SpaceXColors.OnBackground,
        secondaryContainer = SpaceXColors.InteractivePressed,
        onSecondaryContainer = SpaceXColors.OnBackground,
        tertiary = SpaceXColors.Success,
        onTertiary = SpaceXColors.OnBackground,
        tertiaryContainer = SpaceXColors.Warning,
        onTertiaryContainer = SpaceXColors.OnBackground,
        background = SpaceXColors.Background,
        onBackground = SpaceXColors.OnBackground,
        surface = SpaceXColors.Surface,
        onSurface = SpaceXColors.OnSurface,
        surfaceVariant = SpaceXColors.SurfaceVariant,
        onSurfaceVariant = SpaceXColors.OnSurfaceVariant,
        error = SpaceXColors.Error,
        onError = SpaceXColors.OnBackground,
        errorContainer = SpaceXColors.Error,
        onErrorContainer = SpaceXColors.OnBackground,
        outline = SpaceXColors.Divider,
        outlineVariant = SpaceXColors.Border,
        scrim = SpaceXColors.Overlay,
        inverseSurface = SpaceXColors.Surface,
        inverseOnSurface = SpaceXColors.OnSurface,
        inversePrimary = SpaceXColors.PrimaryVariant,
        surfaceDim = SpaceXColors.Surface,
        surfaceBright = SpaceXColors.SurfaceVariant,
        surfaceContainerLowest = SpaceXColors.Background,
        surfaceContainerLow = SpaceXColors.Surface,
        surfaceContainer = SpaceXColors.SurfaceVariant,
        surfaceContainerHigh = SpaceXColors.SurfaceVariant,
        surfaceContainerHighest = SpaceXColors.SurfaceVariant,
    )

@Composable
fun SpaceXTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SpaceXTypography.Typography,
        content = content,
    )
}
