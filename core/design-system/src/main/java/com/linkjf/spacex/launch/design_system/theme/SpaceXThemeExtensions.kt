package com.linkjf.spacex.launch.design_system.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun SpaceXThemeColors(): SpaceXColors = SpaceXColors

@Composable
fun SpaceXThemeSpacing(): SpaceXSpacing = SpaceXSpacing

@Composable
fun SpaceXThemeIcons(): SpaceXIcons = SpaceXIcons

// Material3 Color Scheme Extensions
@Composable
fun SpaceXPrimary(): Color = MaterialTheme.colorScheme.primary

@Composable
fun SpaceXOnPrimary(): Color = MaterialTheme.colorScheme.onPrimary

@Composable
fun SpaceXBackground(): Color = MaterialTheme.colorScheme.background

@Composable
fun SpaceXOnBackground(): Color = MaterialTheme.colorScheme.onBackground

@Composable
fun SpaceXSurface(): Color = MaterialTheme.colorScheme.surface

@Composable
fun SpaceXOnSurface(): Color = MaterialTheme.colorScheme.onSurface

@Composable
fun SpaceXSurfaceVariant(): Color = MaterialTheme.colorScheme.surfaceVariant

@Composable
fun SpaceXOnSurfaceVariant(): Color = MaterialTheme.colorScheme.onSurfaceVariant

@Composable
fun SpaceXOutline(): Color = MaterialTheme.colorScheme.outline

@Composable
fun SpaceXError(): Color = MaterialTheme.colorScheme.error

@Composable
fun SpaceXOnError(): Color = MaterialTheme.colorScheme.onError
