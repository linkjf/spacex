package com.linkjf.spacex.launch.designsystem.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

data class SpaceXDesignTokens(
    val colors: SpaceXColors = SpaceXColors,
    val spacing: SpaceXSpacing = SpaceXSpacing,
    val icons: SpaceXIcons = SpaceXIcons,
)

val LocalSpaceXDesignTokens =
    compositionLocalOf {
        SpaceXDesignTokens(
            colors = SpaceXColors,
            spacing = SpaceXSpacing,
            icons = SpaceXIcons,
        )
    }

@Composable
fun SpaceXDesignTokens(): SpaceXDesignTokens = LocalSpaceXDesignTokens.current

@Composable
fun SpaceXColors(): SpaceXColors = SpaceXDesignTokens().colors

@Composable
fun SpaceXSpacing(): SpaceXSpacing = SpaceXDesignTokens().spacing

@Composable
fun SpaceXIcons(): SpaceXIcons = SpaceXDesignTokens().icons
