package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme

@Composable
fun SpaceXLaunchesLayout(
    title: String,
    tabs: List<String>,
    selectedTabIndex: Int,
    onSearchClick: () -> Unit,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(SpaceXSpacing.Medium),
    ) {
        SpaceXScreenHeader(
            title = title,
            onSearchClick = onSearchClick,
        )

        SpaceXTabSelector(
            tabs = tabs,
            selectedIndex = selectedTabIndex,
            onTabSelected = onTabSelected,
        )

        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchesLayoutPreview() {
    SpaceXTheme {
        SpaceXLaunchesLayout(
            title = "Launches",
            tabs = listOf("Upcoming", "Pack"),
            selectedTabIndex = 0,
            onSearchClick = { },
            onTabSelected = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchesLayoutSecondTabPreview() {
    SpaceXTheme {
        SpaceXLaunchesLayout(
            title = "Launches",
            tabs = listOf("Upcoming", "Pack"),
            selectedTabIndex = 1,
            onSearchClick = { },
            onTabSelected = { },
        )
    }
}
