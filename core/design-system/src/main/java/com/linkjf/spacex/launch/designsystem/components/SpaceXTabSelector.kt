package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTypography

@Composable
fun SpaceXTabSelector(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    activeTabColor: Color = SpaceXColors.TabActiveBackground,
    inactiveTabColor: Color = SpaceXColors.TabBackground,
    activeTextColor: Color = SpaceXColors.TabActiveText,
    inactiveTextColor: Color = SpaceXColors.TabInactiveText,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = SpaceXSpacing.HeaderPadding),
        horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.TabMargin),
    ) {
        tabs.forEachIndexed { index, tab ->
            val isSelected = index == selectedIndex

            Box(
                modifier =
                    Modifier
                        .clip(RoundedCornerShape(SpaceXSpacing.BorderRadiusLarge))
                        .background(
                            color = if (isSelected) activeTabColor else inactiveTabColor,
                        ).clickable { onTabSelected(index) }
                        .padding(
                            horizontal = SpaceXSpacing.TabPadding,
                            vertical = SpaceXSpacing.Small2,
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = tab,
                    style = SpaceXTypography.Typography.labelLarge,
                    color = if (isSelected) activeTextColor else inactiveTextColor,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXTabSelectorPreview() {
    SpaceXTheme {
        SpaceXTabSelector(
            tabs = listOf("Upcoming", "Pack"),
            selectedIndex = 0,
            onTabSelected = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXTabSelectorSecondSelectedPreview() {
    SpaceXTheme {
        SpaceXTabSelector(
            tabs = listOf("Upcoming", "Pack"),
            selectedIndex = 1,
            onTabSelected = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXTabSelectorThreeTabsPreview() {
    SpaceXTheme {
        SpaceXTabSelector(
            tabs = listOf("Upcoming", "Pack", "History"),
            selectedIndex = 1,
            onTabSelected = { },
        )
    }
}
