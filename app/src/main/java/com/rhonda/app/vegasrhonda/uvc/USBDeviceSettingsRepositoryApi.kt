package com.rhonda.app.vegasrhonda.uvc

interface USBDeviceSettingsRepositoryApi {

    fun getPreviewSettings(deviceKey: String): PreviewSettingsDb

    fun setPreviewSettings(deviceKey: String, settings: PreviewSettingsDb)

    data class PreviewSettingsDb(
        val isShowInfoLabels: Boolean = true,
        val isKeepAspectRatio: Boolean = true,
        val resolutionIndex: Int = 0,
        val fpsIndex: Int = -1,
    )
}