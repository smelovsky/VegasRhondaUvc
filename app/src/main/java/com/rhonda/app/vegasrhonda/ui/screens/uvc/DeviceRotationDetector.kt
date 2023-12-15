package com.rhonda.app.vegasrhonda.ui.screens.uvc

import android.view.OrientationEventListener
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext

@Composable
fun DeviceRotationDetector(
    onDeviceRotatedDegrees: (Int) -> Unit,
    onLayoutOrientationChanged: (Int) -> Unit,
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current

    var currentOrientation by remember { mutableStateOf(configuration.orientation) }

    if (currentOrientation != configuration.orientation) {
        onLayoutOrientationChanged(configuration.orientation)
        currentOrientation = configuration.orientation
    }

    DisposableEffect(Unit) {
        val listener = object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                onDeviceRotatedDegrees(orientation)
            }
        }
        if (listener.canDetectOrientation()) {
            listener.enable()
        }
        onDispose {
            listener.disable()
        }
    }
}