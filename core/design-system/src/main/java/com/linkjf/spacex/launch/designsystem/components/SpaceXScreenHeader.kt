package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
fun SpaceXScreenHeader(
    title: String,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleColor: Color = SpaceXColors.OnBackground,
    searchIconColor: Color = SpaceXColors.OnBackground,
    searchContentDescription: String = stringResource(R.string.screen_header_search_content_description),
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    horizontal = SpaceXSpacing.HeaderPadding,
                    vertical = SpaceXSpacing.Small2,
                ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = SpaceXTypography.Typography.displayLarge,
            color = titleColor,
            modifier = Modifier.weight(1f),
        )

        IconButton(
            onClick = onSearchClick,
            modifier = Modifier.size(SpaceXSpacing.IconMedium),
        ) {
            Icon(
                imageVector = SpaceXIcons.Search,
                contentDescription = searchContentDescription,
                tint = searchIconColor,
                modifier = Modifier.size(SpaceXSpacing.IconSmall),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXScreenHeaderPreview() {
    SpaceXTheme {
        SpaceXScreenHeader(
            title = "Launches",
            onSearchClick = { },
            searchContentDescription = "Search",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXScreenHeaderLongTitlePreview() {
    SpaceXTheme {
        SpaceXScreenHeader(
            title = "Upcoming SpaceX Launches",
            onSearchClick = { },
            searchContentDescription = "Search",
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXScreenHeaderDarkPreview() {
    SpaceXTheme {
        SpaceXScreenHeader(
            title = "Launches",
            onSearchClick = { },
            titleColor = SpaceXColors.OnBackground,
            searchIconColor = SpaceXColors.Primary,
            searchContentDescription = "Search",
        )
    }
}
