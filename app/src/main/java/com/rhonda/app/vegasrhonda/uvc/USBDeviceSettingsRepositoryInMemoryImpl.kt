package com.rhonda.app.vegasrhonda.uvc

import java.util.concurrent.ConcurrentHashMap

private typealias PreviewSettingsDb = USBDeviceSettingsRepositoryApi.PreviewSettingsDb

class USBDeviceSettingsRepositoryInMemoryImpl : USBDeviceSettingsRepositoryApi {

    private var settingsMap = ConcurrentHashMap<String, PreviewSettingsDb>()

    override fun getPreviewSettings(deviceKey: String): PreviewSettingsDb {
        return settingsMap[deviceKey] ?: PreviewSettingsDb()
    }

    override fun setPreviewSettings(deviceKey: String, settings: PreviewSettingsDb) {
        settingsMap[deviceKey] = settings
    }
}