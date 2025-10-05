package com.linkjf.spacex.launch.design_system.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import com.linkjf.spacex.launch.design_system.theme.SpaceXColors
import com.linkjf.spacex.launch.design_system.theme.SpaceXSpacing
import com.linkjf.spacex.launch.design_system.theme.SpaceXTheme
import com.linkjf.spacex.launch.design_system.theme.SpaceXTypography

@Composable
fun SpaceXInteractiveTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedBackgroundColor: Color = SpaceXColors.TabActiveBackground,
    unselectedBackgroundColor: Color = SpaceXColors.TabBackground,
    selectedTextColor: Color = SpaceXColors.TabActiveText,
    unselectedTextColor: Color = SpaceXColors.TabInactiveText,
    enabled: Boolean = true
) {
    val backgroundColor by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = tween(durationMillis = SpaceXSpacing.InteractiveTabAnimationDuration),
        label = "backgroundColor"
    )

    val animatedBackgroundColor = Color(
        red = unselectedBackgroundColor.red + (selectedBackgroundColor.red - unselectedBackgroundColor.red) * backgroundColor,
        green = unselectedBackgroundColor.green + (selectedBackgroundColor.green - unselectedBackgroundColor.green) * backgroundColor,
        blue = unselectedBackgroundColor.blue + (selectedBackgroundColor.blue - unselectedBackgroundColor.blue) * backgroundColor,
        alpha = unselectedBackgroundColor.alpha + (selectedBackgroundColor.alpha - unselectedBackgroundColor.alpha) * backgroundColor
    )

    val animatedTextColor = Color(
        red = unselectedTextColor.red + (selectedTextColor.red - unselectedTextColor.red) * backgroundColor,
        green = unselectedTextColor.green + (selectedTextColor.green - unselectedTextColor.green) * backgroundColor,
        blue = unselectedTextColor.blue + (selectedTextColor.blue - unselectedTextColor.blue) * backgroundColor,
        alpha = unselectedTextColor.alpha + (selectedTextColor.alpha - unselectedTextColor.alpha) * backgroundColor
    )

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(SpaceXSpacing.BorderRadiusLarge))
            .background(animatedBackgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                enabled = enabled,
                onClickLabel = text,
                role = Role.Tab,
                onClick = onClick
            )
            .padding(
                horizontal = SpaceXSpacing.TabPadding,
                vertical = SpaceXSpacing.Small2
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SpaceXTypography.Typography.labelLarge,
            color = animatedTextColor
        )
    }
}

@Composable
fun SpaceXInteractiveTabSelector(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    activeTabColor: Color = SpaceXColors.TabActiveBackground,
    inactiveTabColor: Color = SpaceXColors.TabBackground,
    activeTextColor: Color = SpaceXColors.TabActiveText,
    inactiveTextColor: Color = SpaceXColors.TabInactiveText
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpaceXSpacing.HeaderPadding),
        horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.TabMargin)
    ) {
        tabs.forEachIndexed { index, tab ->
            SpaceXInteractiveTab(
                text = tab,
                isSelected = index == selectedIndex,
                onClick = { onTabSelected(index) },
                selectedBackgroundColor = activeTabColor,
                unselectedBackgroundColor = inactiveTabColor,
                selectedTextColor = activeTextColor,
                unselectedTextColor = inactiveTextColor
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXInteractiveTabPreview() {
    SpaceXTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(SpaceXSpacing.TabMargin),
            modifier = Modifier.padding(SpaceXSpacing.CardPadding)
        ) {
            SpaceXInteractiveTab(
                text = "Upcoming",
                isSelected = true,
                onClick = { }
            )
            SpaceXInteractiveTab(
                text = "Pack",
                isSelected = false,
                onClick = { }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXInteractiveTabSelectorPreview() {
    SpaceXTheme {
        SpaceXInteractiveTabSelector(
            tabs = listOf("Upcoming", "Pack"),
            selectedIndex = 0,
            onTabSelected = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXInteractiveTabSelectorSecondSelectedPreview() {
    SpaceXTheme {
        SpaceXInteractiveTabSelector(
            tabs = listOf("Upcoming", "Pack"),
            selectedIndex = 1,
            onTabSelected = { }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXInteractiveTabCustomPreview() {
    SpaceXTheme {
        SpaceXInteractiveTab(
            text = "Custom Tab",
            isSelected = true,
            onClick = { },
            selectedBackgroundColor = SpaceXColors.Success,
            unselectedBackgroundColor = SpaceXColors.SurfaceVariant,
            selectedTextColor = SpaceXColors.OnBackground,
            unselectedTextColor = SpaceXColors.OnSurfaceVariant,
            modifier = Modifier.padding(SpaceXSpacing.CardPadding)
        )
    }
}
