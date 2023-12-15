package com.rhonda.app.vegasrhonda.uvc

data class PreviewSettingsViewState(
    val isShowInfoLabels: Boolean = true,
    val isKeepAspectRatio: Boolean = true,
    val availableResolutions: List<String> = emptyList(),
    val resolutionSelectedIndex: Int = -1,
    val availableFps: List<String> = emptyList(),
    val fpsSelectedIndex: Int = -1,
)
