package com.rhonda.app.vegasrhonda.ui.screens.uvc

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhonda.R
import com.rhonda.app.vegasrhonda.uvc.AttachedDeviceViewState
import com.rhonda.app.vegasrhonda.uvc.PreviewSettingsViewState
import com.rhonda.app.vegasrhonda.uvc.USBDeviceSettingsRepositoryApi
import com.rhonda.sdk.usb.uvc.api.UVCCameraApi
import com.rhonda.sdk.usb.uvc.api.types.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

object VideoContainers {

    private val containers = ConcurrentHashMap<String, VideoContainerViewHolder>()

    @Synchronized
    fun get(uniqueId: String): VideoContainerViewHolder {
        if (!containers.containsKey(uniqueId)) {
            containers[uniqueId] = VideoContainerViewHolder()
        }
        return containers[uniqueId]!!
    }

    @Synchronized
    fun remove(uniqueId: String): Boolean {
        return containers.remove(uniqueId)?.let { true } ?: false
    }
}

class VideoContainerViewHolder {

    @Volatile
    var container: VideoContainer? = null

    fun provideContainerView(context: Context): View {
        return container?.containerView
            ?: inflateContainerView(context).also {
                container = VideoContainer(
                    it.findViewById(R.id.video_view),
                    it.findViewById(R.id.video_container),
                )
            }
    }

    @SuppressLint("InflateParams")
    private fun inflateContainerView(context: Context): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        return inflater.inflate(R.layout.video_container_layout, null, false)
    }

    data class VideoContainer(
        val surfaceView: SurfaceView,
        val containerView: ViewGroup,
    )
}

@HiltViewModel
class UvcViewModel @Inject constructor(
    private val uvcCameraApi: UVCCameraApi,
    private val settingsRepositoryApi: USBDeviceSettingsRepositoryApi,
    ) : ViewModel() {

    private val classTag = this::class.java.simpleName

    @Volatile
    private var resumeJob: Job? = null

    @Volatile
    private var connectingJob: Job? = null

    @Volatile
    private var previewJob: Job? = null

    // unique device key for settings repository
    private val attachedDeviceKey: String
        get() = _attachedDeviceInfoState.value?.let {
            "${it.productName}_${it.vendorId}_${it.productId}"
        } ?: "unknown"

    val previewContainerId: String = UUID.randomUUID().toString()
    val fullscreenContainerId: String = UUID.randomUUID().toString()

    private val _attachedDeviceInfoState = MutableStateFlow<AttachedDeviceViewState?>(null)
    val attachedDeviceInfoState: StateFlow<AttachedDeviceViewState?> = _attachedDeviceInfoState

    val isConnectedState: StateFlow<Boolean> = uvcCameraApi.isConnected

    private val _lastErrorState = MutableStateFlow("")
    val lastErrorState: StateFlow<String> = _lastErrorState

    private val _sizeInfoState = MutableStateFlow(emptyList<String>())
    val sizeInfoState: StateFlow<List<String>> = _sizeInfoState

    private val _streamStatState = MutableStateFlow(emptyList<String>())
    val streamStatState: StateFlow<List<String>> = _streamStatState

    private val _previewSettingsState = MutableStateFlow(PreviewSettingsViewState())
    val previewSettingsState: StateFlow<PreviewSettingsViewState> = _previewSettingsState

    init {

        viewModelScope.launch(Dispatchers.Default) {
            uvcCameraApi.attachedDevice.collect { device ->
                if (device != null) {
                    _attachedDeviceInfoState.value = AttachedDeviceViewState(
                        vendorId = device.vendorId
                            .coerceAtLeast(0)
                            .toString(16)
                            .padStart(4, '0'),
                        productId = device.productId
                            .coerceAtLeast(0)
                            .toString(16)
                            .padStart(4, '0'),
                        productName = device.productName ?: "unknown",
                    )
                } else {
                    _attachedDeviceInfoState.value = null
                }
            }
        }

        viewModelScope.launch(Dispatchers.Default) {
            uvcCameraApi.lastError.collect { error ->
                Log.i("uvc", "lastError.collect: $error")
                _lastErrorState.value = when (error) {
                    Errors.NO_PERMISSIONS_GRANTED -> "No permissions granted"
                    Errors.UVC_CAMERA_OPEN_FAIL -> "Device open fail, seems it is not UVC device"
                    Errors.PERMISSIONS_ANDROID_10_PROBLEM -> "No permissions granted. Maybe this is a problem of Android 10 version"
                    null -> ""
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }

    // USB device screen

    fun onResume() {
        Log.i("uvc", "onResume")
        resumeJob?.cancel()
        resumeJob = viewModelScope.launch(Dispatchers.Default) {

            launch {
                uvcCameraApi.attachedDevice.collect { device ->
                    if (device != null) {
                        uvcConnect()
                    }
                }
            }

            launch {
                uvcCameraApi.isConnected.collect { isConnected ->
                    if (isConnected) {
                        // updates available formats for just now connected camera
                        updatePreviewSettingsState()
                    }
                }
            }

            launch {
                uvcCameraApi.previewStat.collect { stat ->
                    if (stat.fps > 0 && stat.bitrate > 0) {
                        _streamStatState.value = listOf("${stat.fps} FPS, ${stat.bitrate} kBit/sec")
                    } else {
                        _streamStatState.value = emptyList()
                    }
                }
            }
        }
    }

    fun onPause() {
        resumeJob?.cancel()
        stopPreview()
        uvcDisconnect()
    }

    fun onReconnectButtonClick() {
        _lastErrorState.value = ""
        uvcDisconnect()
        uvcConnect()
    }

    // preview video

    fun onPreviewResume() {
        startPreview(previewContainerId)
    }

    fun onPreviewPause() {
        stopPreview()
    }

    fun onPreviewOrientationChanged() {
        if (uvcCameraApi.isConnected.value) {
            stopPreview()
            startPreview(previewContainerId)
        }
    }

    // fullscreen preview

    fun onFullscreenResume() {
        startPreview(fullscreenContainerId, 1000L)
    }

    fun onFullscreenPause() {
        stopPreview()
        startPreview(previewContainerId, 1000L)
    }

    // preview settings

    fun onPreviewResolutionSelect(index: Int) {
        Log.d(classTag, "onPreviewResolutionSelect: $index")
        settingsRepositoryApi.setPreviewSettings(
            attachedDeviceKey,
            settingsRepositoryApi.getPreviewSettings(attachedDeviceKey).copy(
                resolutionIndex = index,
                fpsIndex = -1,
            )
        )
        updatePreviewSettingsState()
    }

    fun onPreviewFpsSelect(index: Int) {
        Log.d(classTag, "onPreviewFpsSelect: $index")
        settingsRepositoryApi.setPreviewSettings(
            attachedDeviceKey,
            settingsRepositoryApi.getPreviewSettings(attachedDeviceKey).copy(fpsIndex = index),
        )
        updatePreviewSettingsState()
    }

    fun onPreviewShowLabelsChecked(checked: Boolean) {
        Log.d(classTag, "onPreviewShowLabelsChecked: $checked")
        settingsRepositoryApi.setPreviewSettings(
            attachedDeviceKey,
            settingsRepositoryApi.getPreviewSettings(attachedDeviceKey).copy(isShowInfoLabels = checked),
        )
        updatePreviewSettingsState()
    }

    fun onPreviewKeepAspectRatioChecked(checked: Boolean) {
        Log.d(classTag, "onPreviewKeepAspectRatioChecked: $checked")
        settingsRepositoryApi.setPreviewSettings(
            attachedDeviceKey,
            settingsRepositoryApi.getPreviewSettings(attachedDeviceKey).copy(isKeepAspectRatio = checked),
        )
        updatePreviewSettingsState()
    }

    private fun updatePreviewSettingsState() {
        Log.d(classTag, "updatePreviewSettings")
        val settings = settingsRepositoryApi.getPreviewSettings(attachedDeviceKey)
        val formats = uvcCameraApi.supportedFormats
        if (settings.resolutionIndex in formats.indices) {
            val supportedFps = formats[settings.resolutionIndex].supportedFps
            val defaultFpsIndex = supportedFps.indexOf(formats[settings.resolutionIndex].defaultFps)
                .coerceIn(supportedFps.indices)
            _previewSettingsState.value = _previewSettingsState.value.copy(
                isShowInfoLabels = settings.isShowInfoLabels,
                isKeepAspectRatio = settings.isKeepAspectRatio,
                availableResolutions = formats.map {
                    "${it.frameFormat.description}, ${it.width}x${it.height}"
                },
                resolutionSelectedIndex = settings.resolutionIndex,
                availableFps = formats[settings.resolutionIndex].supportedFps.map { it.toString() },
                fpsSelectedIndex = if (settings.fpsIndex >= 0)
                    settings.fpsIndex.coerceIn(supportedFps.indices)
                else
                    defaultFpsIndex,
            )
        }
    }

    private fun startPreview(containerId: String, initialDelay: Long = 500L) {
        Log.i(classTag, "startPreview: $containerId")
        previewJob?.cancel()
        previewJob = viewModelScope.launch(Dispatchers.Default) {
            delay(initialDelay)
            VideoContainers.get(containerId).container?.let {

                var previewInfo = ""
                var previewStarted = false

                val formats = uvcCameraApi.supportedFormats

                if (formats.isNotEmpty()) {

                    val previewSettings = settingsRepositoryApi.getPreviewSettings(attachedDeviceKey)

                    val format = when {
                        previewSettings.resolutionIndex >= 0 -> formats[previewSettings.resolutionIndex]
                        else -> formats.first()
                    }

                    val fps = when {
                        previewSettings.fpsIndex in format.supportedFps.indices -> format.supportedFps[previewSettings.fpsIndex]
                        format.defaultFps > 0 -> format.defaultFps
                        else -> format.supportedFps.getOrElse(0) { 0 }
                    }

                    previewInfo = "${format.frameFormat.description}  ${format.width} x ${format.height} @ $fps"

                    previewStarted = uvcCameraApi.startPreview(
                        surfaceView = it.surfaceView,
                        containerView = it.containerView,
                        frameFormat = format.frameFormat,
                        width = format.width,
                        height = format.height,
                        minFps = fps,
                        maxFps = fps,
                        keepAspectRatio = previewSettings.isKeepAspectRatio,
                    )
                }

                if (previewStarted) {
                    _sizeInfoState.value = listOf(previewInfo)
                } else {
                    _sizeInfoState.value = listOf("PREVIEW START ERROR")
                }
            }
        }
    }

    private fun stopPreview() {
        Log.i(classTag, "stopPreview")
        previewJob?.cancel()
        uvcCameraApi.stopPreview()
        _sizeInfoState.value = emptyList()
        _streamStatState.value = emptyList()
    }

    private fun uvcConnect() {
        Log.i(classTag, "uvcConnect")
        connectingJob?.cancel()
        connectingJob = viewModelScope.launch(Dispatchers.Default) {
            delay(500L)
            uvcCameraApi.connect()
        }
    }

    private fun uvcDisconnect() {
        Log.i(classTag, "uvcDisconnect")
        connectingJob?.cancel()
        uvcCameraApi.disconnect()
        _lastErrorState.value = ""
    }

}