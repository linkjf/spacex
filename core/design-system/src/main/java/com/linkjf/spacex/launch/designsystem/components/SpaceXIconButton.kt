package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.linkjf.spacex.launch.designsystem.R
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXIcons
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme

@Composable
fun SpaceXIconButton(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.content_description_button),
    iconColor: Color = SpaceXColors.OnBackground,
    backgroundColor: Color = Color.Transparent,
    iconSize: Dp = SpaceXSpacing.IconSmall,
    buttonSize: Dp = SpaceXSpacing.TouchTarget,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(buttonSize),
        colors =
            IconButtonDefaults.iconButtonColors(
                containerColor = backgroundColor,
                contentColor = iconColor,
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun SpaceXIconButtonWithRipple(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(R.string.content_description_button),
    iconColor: Color = SpaceXColors.OnBackground,
    backgroundColor: Color = Color.Transparent,
    iconSize: Dp = SpaceXSpacing.IconSmall,
    buttonSize: Dp = SpaceXSpacing.TouchTarget,
    enabled: Boolean = true,
) {
    IconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(buttonSize),
        colors =
            IconButtonDefaults.iconButtonColors(
                containerColor = backgroundColor,
                contentColor = iconColor,
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(iconSize),
        )
    }
}

@Composable
fun SpaceXSearchButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = SpaceXColors.OnBackground,
    backgroundColor: Color = Color.Transparent,
    enabled: Boolean = true,
    contentDescription: String = stringResource(R.string.content_description_search),
) {
    SpaceXIconButton(
        onClick = onClick,
        icon = SpaceXIcons.Search,
        contentDescription = contentDescription,
        modifier = modifier,
        iconColor = iconColor,
        backgroundColor = backgroundColor,
        iconSize = SpaceXSpacing.IconSmall,
        buttonSize = SpaceXSpacing.IconMedium,
        enabled = enabled,
    )
}

@Composable
fun SpaceXPlayButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = SpaceXColors.OnSurface,
    backgroundColor: Color = Color.Transparent,
    enabled: Boolean = true,
    contentDescription: String = stringResource(R.string.content_description_watch),
) {
    SpaceXIconButton(
        onClick = onClick,
        icon = SpaceXIcons.PlayCircle,
        contentDescription = contentDescription,
        modifier = modifier,
        iconColor = iconColor,
        backgroundColor = backgroundColor,
        iconSize = SpaceXSpacing.IconSmall,
        buttonSize = SpaceXSpacing.IconSmall,
        enabled = enabled,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXIconButtonPreview() {
    SpaceXTheme {
        SpaceXIconButton(
            onClick = { },
            icon = SpaceXIcons.Search,
            contentDescription = "Search",
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXIconButtonWithRipplePreview() {
    SpaceXTheme {
        SpaceXIconButtonWithRipple(
            onClick = { },
            icon = SpaceXIcons.PlayCircle,
            contentDescription = "Play",
            backgroundColor = SpaceXColors.Primary.copy(alpha = 0.1f),
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXSearchButtonPreview() {
    SpaceXTheme {
        SpaceXSearchButton(
            onClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            contentDescription = "Search launches",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXPlayButtonPreview() {
    SpaceXTheme {
        SpaceXPlayButton(
            onClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            contentDescription = "Watch launch",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXIconButtonCustomPreview() {
    SpaceXTheme {
        SpaceXIconButton(
            onClick = { },
            icon = SpaceXIcons.Wind,
            contentDescription = "Wind",
            iconColor = SpaceXColors.Primary,
            backgroundColor = SpaceXColors.SurfaceVariant,
            iconSize = SpaceXSpacing.IconMedium,
            buttonSize = SpaceXSpacing.TouchTarget,
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXIconButtonDisabledPreview() {
    SpaceXTheme {
        SpaceXIconButton(
            onClick = { },
            icon = SpaceXIcons.Search,
            contentDescription = "Search",
            iconColor = SpaceXColors.InteractiveDisabled,
            enabled = false,
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
        )
    }
}
