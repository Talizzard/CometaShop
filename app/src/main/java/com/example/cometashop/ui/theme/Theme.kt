package com.example.cometashop.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CometaColorScheme = darkColorScheme(
    primary = CometaYellow,
    background = CometaBlack,
    surface = CometaBlack,
    onPrimary = CometaBlack,
    onBackground = CometaWhite,
    onSurface = CometaWhite
)

@Composable
fun CometaShopTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CometaColorScheme,
        content = content
    )
}
