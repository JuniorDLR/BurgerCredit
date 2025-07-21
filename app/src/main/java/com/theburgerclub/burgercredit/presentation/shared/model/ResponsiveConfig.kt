package com.theburgerclub.burgercredit.presentation.shared.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class ResponsiveConfig(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val searchFieldHeight: Dp,
    val cardHeight: Dp,
    val fontSize: TextUnit,
    val titleFontSize: TextUnit
)

@Composable
fun rememberResponsiveConfig(): ResponsiveConfig {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp
    val isLandscape = screenWidth > screenHeight
    return ResponsiveConfig(
        horizontalPadding = when {
            screenWidth < 320 -> 8.dp
            screenWidth < 480 -> 16.dp
            screenWidth > 720 -> 48.dp
            else -> 20.dp
        },
        verticalPadding = when {
            isLandscape -> 8.dp
            screenHeight < 600 -> 8.dp
            screenHeight < 800 -> 16.dp
            screenWidth > 720 -> 32.dp
            else -> 20.dp
        },
        searchFieldHeight = when {
            isLandscape -> 56.dp
            screenHeight < 600 -> 48.dp
            else -> 52.dp
        },
        cardHeight = when {
            screenHeight < 600 -> 56.dp
            screenHeight < 800 -> 64.dp
            else -> 70.dp
        },
        fontSize = when {
            screenWidth < 320 -> 13.sp
            screenWidth < 480 -> 15.sp
            screenWidth > 720 -> 20.sp
            else -> 16.sp
        },
        titleFontSize = when {
            isLandscape -> 16.sp
            screenWidth < 320 -> 14.sp
            screenWidth < 480 -> 16.sp
            screenWidth > 720 -> 22.sp
            else -> 18.sp
        }
    )
} 