/*
 * Copyright (C) 2021 Rhonda Software.
 * All rights reserved.
 */

package com.rhonda.sdk.ui.compose.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InfoLabel(
    modifier: Modifier = Modifier,
    textLines: List<String>,
    backgroundAlpha: Float = 0.5f,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = Color.Gray.copy(alpha = backgroundAlpha),
    ) {
        Column {
            textLines.forEach { text ->
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 1.dp),
                    text = text,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
