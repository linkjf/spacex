package com.linkjf.spacex.launch.design_system.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object SpaceXTypography {
    private val Roboto = FontFamily.Default
    
    val Typography = Typography(
        displayLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W600,
            fontSize = 32.sp,
            lineHeight = 37.5.sp,
            letterSpacing = 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W600,
            fontSize = 28.sp,
            lineHeight = 32.8.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W600,
            fontSize = 24.sp,
            lineHeight = 28.1.sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W600,
            fontSize = 20.sp,
            lineHeight = 23.4.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W600,
            fontSize = 18.sp,
            lineHeight = 21.1.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            lineHeight = 18.8.sp,
            letterSpacing = 0.sp
        ),
        titleLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp,
            lineHeight = 18.8.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W500,
            fontSize = 14.sp,
            lineHeight = 16.4.sp,
            letterSpacing = 0.sp
        ),
        titleSmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W500,
            fontSize = 12.sp,
            lineHeight = 14.1.sp,
            letterSpacing = 0.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W300,
            fontSize = 16.sp,
            lineHeight = 18.8.sp,
            letterSpacing = 0.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W300,
            fontSize = 14.sp,
            lineHeight = 16.4.sp,
            letterSpacing = 0.sp
        ),
        bodySmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W300,
            fontSize = 12.sp,
            lineHeight = 14.1.sp,
            letterSpacing = 0.sp
        ),
        labelLarge = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            fontSize = 14.sp,
            lineHeight = 16.4.sp,
            letterSpacing = 0.sp
        ),
        labelMedium = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            fontSize = 12.sp,
            lineHeight = 14.1.sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = Roboto,
            fontWeight = FontWeight.W400,
            fontSize = 10.sp,
            lineHeight = 11.7.sp,
            letterSpacing = 0.sp
        )
    )
    
    // Custom styles for SpaceX components
    val weatherMetric = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        lineHeight = 18.8.sp,
        letterSpacing = 0.sp
    )
    
    val countdownTimer = TextStyle(
        fontFamily = Roboto,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp,
        lineHeight = 23.4.sp,
        letterSpacing = 0.sp
    )
}
