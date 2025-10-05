package com.linkjf.spacex.launch.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.compose.SubcomposeAsyncImage
import com.linkjf.spacex.launch.designsystem.theme.SpaceXColors
import com.linkjf.spacex.launch.designsystem.theme.SpaceXIcons
import com.linkjf.spacex.launch.designsystem.theme.SpaceXSpacing
import com.linkjf.spacex.launch.designsystem.theme.SpaceXTheme

@Composable
fun SpaceXLaunchImage(
    imageUrl: String?,
    contentDescription: String,
    modifier: Modifier = Modifier,
    placeholderColor: Color = SpaceXColors.ImagePlaceholder,
    loadingColor: Color = SpaceXColors.Primary,
    errorColor: Color = SpaceXColors.OnSurfaceVariant,
    imageSize: Dp = SpaceXSpacing.LaunchImageDefaultSize,
    strokeWidth: Dp = SpaceXSpacing.ProgressIndicatorStrokeWidth,
) {
    Box(
        modifier =
            modifier
                .size(imageSize)
                .clip(RoundedCornerShape(SpaceXSpacing.BorderRadiusSmall))
                .background(placeholderColor),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl != null) {
            SubcomposeAsyncImage(
                model = imageUrl,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = {
                    CircularProgressIndicator(
                        modifier = Modifier.size(SpaceXSpacing.IconMedium),
                        color = loadingColor,
                        strokeWidth = strokeWidth,
                    )
                },
                error = {
                    Icon(
                        imageVector = SpaceXIcons.PlayCircle,
                        contentDescription = contentDescription,
                        tint = errorColor,
                        modifier = Modifier.size(SpaceXSpacing.IconMedium),
                    )
                },
            )
        } else {
            Icon(
                imageVector = SpaceXIcons.PlayCircle,
                contentDescription = contentDescription,
                tint = errorColor,
                modifier = Modifier.size(SpaceXSpacing.IconMedium),
            )
        }
    }
}

@Composable
fun SpaceXLaunchImagePlaceholder(
    contentDescription: String,
    modifier: Modifier = Modifier,
    placeholderColor: Color = SpaceXColors.ImagePlaceholder,
    iconColor: Color = SpaceXColors.OnSurfaceVariant,
    imageSize: Dp = SpaceXSpacing.LaunchImageDefaultSize,
) {
    Box(
        modifier =
            modifier
                .size(imageSize)
                .clip(RoundedCornerShape(SpaceXSpacing.BorderRadiusSmall))
                .background(placeholderColor),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = SpaceXIcons.PlayCircle,
            contentDescription = contentDescription,
            tint = iconColor,
            modifier = Modifier.size(SpaceXSpacing.IconMedium),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchImagePreview() {
    SpaceXTheme {
        SpaceXLaunchImage(
            imageUrl = null,
            contentDescription = "Launch patch",
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            imageSize = SpaceXSpacing.LaunchImageDefaultSize,
            strokeWidth = SpaceXSpacing.ProgressIndicatorStrokeWidth,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchImagePlaceholderPreview() {
    SpaceXTheme {
        SpaceXLaunchImagePlaceholder(
            contentDescription = "Launch patch placeholder",
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            imageSize = SpaceXSpacing.LaunchImageDefaultSize,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF151515)
@Composable
private fun SpaceXLaunchImageWithUrlPreview() {
    SpaceXTheme {
        SpaceXLaunchImage(
            imageUrl = "https://images2.imgbox.com/94/f2/NN6z45OK_o.png",
            contentDescription = "FalconSat launch patch",
            modifier = Modifier.padding(SpaceXSpacing.CardPadding),
            imageSize = SpaceXSpacing.LaunchImageDefaultSize,
            strokeWidth = SpaceXSpacing.ProgressIndicatorStrokeWidth,
        )
    }
}
