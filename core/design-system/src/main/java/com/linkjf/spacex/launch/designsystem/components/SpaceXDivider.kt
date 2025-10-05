package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme

@Composable
fun SpaceXDivider(
    modifier: Modifier = Modifier,
    color: Color = SpaceXColors.Divider,
    thickness: Float = SpaceXSpacing.DividerThicknessDefault,
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        thickness = thickness.dp,
        color = color,
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXDividerPreview() {
    SpaceXTheme {
        SpaceXDivider(
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXDividerCustomPreview() {
    SpaceXTheme {
        SpaceXDivider(
            color = SpaceXColors.Primary,
            thickness = 2f,
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
        )
    }
}
