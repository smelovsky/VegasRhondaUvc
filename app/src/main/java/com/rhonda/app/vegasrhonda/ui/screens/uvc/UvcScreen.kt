package com.rhonda.app.vegasrhonda.ui.screens.uvc

import android.annotation.SuppressLint
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rhonda.R
import com.rhonda.ui.common.MainScaffold
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Fullscreen
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.rhonda.app.vegasrhonda.uvc.AttachedDeviceViewState
import com.rhonda.app.vegasrhonda.uvcViewModel
import com.rhonda.sdk.ui.compose.widgets.InfoLabel
import com.rhonda.sdk.ui.compose.widgets.RoundIconButton
import com.rhonda.ui.common.AndroidUtils.getActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun UvcRoute(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier

) {

    val allowedNavigateBack = remember() { navigateBack }
    val allowedNavigateToHome = remember() { navigateToHome }
    val allowedNavigateToSettings = remember() { navigateToSettings }

    UvcScreen(
        navigateBack = { allowedNavigateBack() },
        navigateToHome = { allowedNavigateToHome() },
        navigateToSettings = { allowedNavigateToSettings() },
        modifier = modifier,
    )

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun UvcScreen(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier
) {

    BackHandler(onBack = navigateBack)

    MainScaffold(
        titleRes = R.string.uvc_screen_title,
        navigateToHome = navigateToHome,
        navigateToSettings = navigateToSettings,
    ) { innerPadding ->

        val context = LocalContext.current

        val scope = rememberCoroutineScope()

        val isConnectedState by uvcViewModel.isConnectedState.collectAsState()
        val lastErrorState by uvcViewModel.lastErrorState.collectAsState()
        val attachedDeviceInfoState by uvcViewModel.attachedDeviceInfoState.collectAsState()

        var settingsOpened by remember { mutableStateOf(false) }

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.Transparent),
        ) {
            if (attachedDeviceInfoState == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Not connected")
                }
            } else {
                if (isConnectedState) {
                    if (settingsOpened) {
                        PreviewSettingsSubScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = uvcViewModel,
                            onClose = {
                                scope.launch {
                                    delay(200L)
                                    settingsOpened = false
                                }
                            },
                        )
                    } else {
                        CameraConnectedSubScreen(
                            modifier = Modifier.fillMaxSize(),
                            viewModel = uvcViewModel,
                            onFullscreenClick = {
                                Toast.makeText(context, "Not implemented yet", Toast.LENGTH_LONG).show()
                                /*
                                UIRouter.push(
                                    Screen.Scaffold.Overlay.LandscapeVideoFullscreen(
                                        containerId = viewModel.fullscreenContainerId,
                                        onResume = {
                                            viewModel.onFullscreenResume()
                                        },
                                        onPause = {
                                            viewModel.onFullscreenPause()
                                            UIRouter.pop()
                                        },
                                    )
                                )
                                */
                            },
                            onSettingsClick = {
                                scope.launch {
                                    delay(200L)
                                    settingsOpened = true
                                }
                            },
                        )
                    }
                } else {
                    AttachedDeviceSubScreen(
                        modifier = Modifier.fillMaxSize(),
                        viewModel = uvcViewModel,
                        attachedDeviceInfo = attachedDeviceInfoState,
                        lastError = lastErrorState,
                    )
                }
            }
        }

        DeviceRotationDetector(
            onDeviceRotatedDegrees = {},
            onLayoutOrientationChanged = {
                uvcViewModel.onPreviewOrientationChanged()
            },
        )

        OnLifecycleEvent { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                uvcViewModel.onResume()
            } else if (event == Lifecycle.Event.ON_PAUSE) {
                uvcViewModel.onPause()
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                uvcViewModel.onPause()
            }
        }


    }
}

@Composable
private fun CameraConnectedSubScreen(
    modifier: Modifier = Modifier,
    viewModel: UvcViewModel,
    onFullscreenClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    val settingsViewState by viewModel.previewSettingsState.collectAsState()
    val usbDeviceInfoState by viewModel.attachedDeviceInfoState.collectAsState()
    val sizeInfoState by viewModel.sizeInfoState.collectAsState()
    val streamStatState by viewModel.streamStatState.collectAsState()

    val deviceInfo = remember(usbDeviceInfoState) { listOf(usbDeviceInfoState?.productName ?: "unknown") }

    KeepScreenOnLocker()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {

        // video preview area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            VideoContainerWidget(viewModel.previewContainerId)
        }

        // labels
        if (settingsViewState.isShowInfoLabels) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                Column {
                    Spacer(Modifier.height(4.dp))
                    InfoLabel(textLines = deviceInfo)
                    Spacer(Modifier.height(4.dp))
                    InfoLabel(textLines = sizeInfoState)
                }
            }
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Column {
                    InfoLabel(textLines = streamStatState)
                    Spacer(Modifier.height(4.dp))
                }
            }
        }

        // settings button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.BottomEnd,
        ) {
            Column {
                RoundIconButton(
                    modifier = Modifier.size(56.dp),
                    icon = Icons.Rounded.Fullscreen,
                    onClick = { onFullscreenClick() },
                )
                Spacer(Modifier.height(16.dp))
                RoundIconButton(
                    modifier = Modifier.size(56.dp),
                    icon = Icons.Rounded.Settings,
                    onClick = { onSettingsClick() },
                )
            }
        }
    }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            viewModel.onPreviewResume()
        } else if (event == Lifecycle.Event.ON_PAUSE) {
            viewModel.onPreviewPause()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onPreviewPause()
        }
    }
}

@Composable
private fun AttachedDeviceSubScreen(
    modifier: Modifier = Modifier,
    viewModel: UvcViewModel,
    attachedDeviceInfo: AttachedDeviceViewState?,
    lastError: String,
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
    ) {
        Spacer(modifier = Modifier.weight(0.8f))

        // device info
        attachedDeviceInfo?.let { info ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Attached device",
                )
                Text(
                    text = info.productName,
                    fontStyle = FontStyle.Italic,
                )
                Text(
                    text = "[vid=${info.vendorId}, pid=${info.productId}]",
                    fontStyle = FontStyle.Italic,
                )
            }
        }

        // show error and refresh button
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    modifier = Modifier.padding(20.dp),
                    text = lastError,
                    color = Color.Red,
                )
            }

            if (lastError.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.End,
                ) {
                    RoundIconButton(
                        modifier = Modifier.size(36.dp),
                        icon = Icons.Rounded.Refresh,
                        backgroundAlpha = 0.7f,
                        onClick = {
                            scope.launch {
                                delay(200L)
                                viewModel.onReconnectButtonClick()
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun KeepScreenOnLocker() {
    val context = LocalContext.current

    DisposableEffect(Unit) {
        getActivity(context)?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            getActivity(context)?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}

@Composable
fun OnLifecycleEvent(onEvent: (owner: LifecycleOwner, event: Lifecycle.Event) -> Unit) {

    val currentOnEvent = rememberUpdatedState(onEvent)
    val lifecycleOwner = LocalLifecycleOwner.current

    val observer = remember {
        LifecycleEventObserver { owner, event ->
            currentOnEvent.value(owner, event)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
}
