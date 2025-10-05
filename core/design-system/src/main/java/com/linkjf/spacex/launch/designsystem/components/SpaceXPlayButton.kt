package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.designsystem.R
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXIcons
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography

@Composable
fun SpaceXPlayButtonWithText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = SpaceXColors.OnSurface,
    textColor: Color = SpaceXColors.OnSurface,
    watchText: String = stringResource(R.string.play_button_watch_text),
    contentDescription: String = stringResource(R.string.play_button_watch_content_description),
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(SpaceXSpacing.IconSmall),
        ) {
            Icon(
                imageVector = SpaceXIcons.PlayCircle,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(SpaceXSpacing.IconSmall),
            )
        }

        Text(
            text = watchText,
            style = SpaceXTypography.Typography.titleLarge,
            color = textColor,
        )
    }
}

@Composable
fun SpaceXPlayButtonVerticalWithText(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = SpaceXColors.OnSurface,
    textColor: Color = SpaceXColors.OnSurface,
    watchText: String = stringResource(R.string.play_button_watch_text),
    contentDescription: String = stringResource(R.string.play_button_watch_content_description),
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Small),
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(SpaceXSpacing.IconMedium),
        ) {
            Icon(
                imageVector = SpaceXIcons.PlayCircle,
                contentDescription = contentDescription,
                tint = iconColor,
                modifier = Modifier.size(SpaceXSpacing.IconSmall),
            )
        }

        Text(
            text = watchText,
            style = SpaceXTypography.Typography.titleLarge,
            color = textColor,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXPlayButtonWithTextPreview() {
    SpaceXTheme {
        SpaceXPlayButtonWithText(
            onClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            watchText = "Watch",
            contentDescription = "Watch launch",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXPlayButtonVerticalWithTextPreview() {
    SpaceXTheme {
        SpaceXPlayButtonVerticalWithText(
            onClick = { },
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            watchText = "Watch",
            contentDescription = "Watch launch",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXPlayButtonWithTextCustomColorsPreview() {
    SpaceXTheme {
        SpaceXPlayButtonWithText(
            onClick = { },
            iconColor = SpaceXColors.Primary,
            textColor = SpaceXColors.Primary,
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            watchText = "Watch",
            contentDescription = "Watch launch",
        )
    }
}
