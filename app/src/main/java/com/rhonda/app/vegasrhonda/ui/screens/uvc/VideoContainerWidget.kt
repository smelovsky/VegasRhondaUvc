package com.rhonda.app.vegasrhonda.ui.screens.uvc

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun VideoContainerWidget(uniqueId: String) {
    AndroidView(
        factory = { context ->
            VideoContainers.get(uniqueId).provideContainerView(context)
        },
        modifier = Modifier.fillMaxSize(),
    )
}