/*
 * Copyright (C) 2021-2022 Rhonda Software.
 * All rights reserved.
 */

package com.rhonda.sdk.ui.compose.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun RoundIconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    enabled: Boolean = true,
    backgroundAlpha: Float = 0.5f,
    imageColor: Color = Color.White,
    onClick: () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .clickable { if (enabled) onClick() },
        color = Color.Gray.copy(alpha = if (enabled) backgroundAlpha else backgroundAlpha / 2f),
    ) {
        Image(
            modifier = Modifier.padding(6.dp),
            imageVector = icon,
            contentDescription = null,
            colorFilter = ColorFilter.tint(imageColor),
            alpha = if (enabled) 1f else 0.4f,
        )
    }
}
