package com.kiit.foodfinder.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class ResponsiveInfo(
    val isSmallPhone: Boolean,
    val isMediumPhone: Boolean,
    val isLargePhone: Boolean,
    val isShortPhone: Boolean,
    val isTallPhone: Boolean,
    
    // Dynamic Paddings
    val globalPadding: Dp,
    val contentSpacing: Dp,
    val cardPadding: Dp,
    
    // Dynamic Text Sizes
    val h1: TextUnit,
    val h2: TextUnit,
    val h3: TextUnit,
    val body: TextUnit,
    val label: TextUnit,
    val micro: TextUnit
)

@Composable
fun rememberResponsiveInfo(): ResponsiveInfo {
    val config = LocalConfiguration.current
    val width = config.screenWidthDp
    val height = config.screenHeightDp
    
    val isSmall = width < 360
    val isMedium = width in 360..411
    val isLarge = width > 411
    val isShort = height < 700
    val isTall = height > 850

    return ResponsiveInfo(
        isSmallPhone = isSmall,
        isMediumPhone = isMedium,
        isLargePhone = isLarge,
        isShortPhone = isShort,
        isTallPhone = isTall,
        
        globalPadding = when {
            isSmall -> 16.dp
            isLarge -> 28.dp
            else -> 24.dp
        },
        
        contentSpacing = when {
            isSmall -> 12.dp
            isShort -> 16.dp
            else -> 24.dp
        },

        cardPadding = if (isSmall) 12.dp else 20.dp,

        h1 = when {
            isSmall -> 22.sp
            isLarge -> 30.sp
            else -> 26.sp
        },
        
        h2 = when {
            isSmall -> 18.sp
            isLarge -> 22.sp
            else -> 20.sp
        },
        
        h3 = when {
            isSmall -> 15.sp
            isLarge -> 18.sp
            else -> 16.sp
        },
        
        body = when {
            isSmall -> 13.sp
            isLarge -> 15.sp
            else -> 14.sp
        },
        
        label = when {
            isSmall -> 11.sp
            isLarge -> 13.sp
            else -> 12.sp
        },
        
        micro = when {
            isSmall -> 9.sp
            else -> 10.sp
        }
    )
}
