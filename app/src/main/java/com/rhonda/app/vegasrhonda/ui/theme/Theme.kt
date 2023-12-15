package com.rhonda.app.vegasrhonda.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fullscreenMode: Boolean = false,
    content: @Composable () -> Unit,
) {
    val systemUiController = rememberSystemUiController()

    LaunchedEffect(fullscreenMode, darkTheme) {
        if (fullscreenMode) {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = false
            )
        } else {
            systemUiController.setSystemBarsColor(
                color = if (darkTheme) DarkColors.background else LightColors.background,
                darkIcons = !darkTheme
            )
        }
    }

    val colors = when {
        fullscreenMode -> (if (darkTheme) DarkColors else LightColors).copy(background = Color.Black)
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colors = colors,
        content = content
    )
}
