/*
 * Copyright (C) 2020-2023 Rhonda Software.
 * All rights reserved.
 */

package com.rhonda.videoplayer.ui.widgets.common

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.delay

@Composable
fun RoundColorIndicator(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colorEnabled: Color,
    colorDisabled: Color,
    blinking: Boolean = false,
    blinkingDelay: Long = 1000L,
) {
    var blinkState by remember { mutableStateOf(true) }

    LaunchedEffect(enabled, blinking, blinkState) {
        if (enabled && blinking) {
            delay(blinkingDelay)
            blinkState = !blinkState
        }
    }

    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = if (enabled && (!blinking || (blinking && blinkState))) {
            colorEnabled
        } else {
            colorDisabled
        },
    ) { }
}
