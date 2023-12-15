/*
 * Copyright (C) 2020-2023 Rhonda Software.
 * All rights reserved.
 */

package com.rhonda.data.repository.impl

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import com.rhonda.data.repository.api.GlobalSettingsRepositoryApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class GlobalSettingsRepositoryImpl(context: Context) : GlobalSettingsRepositoryApi {

    private val classTag = this::class.java.simpleName

    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(GlobalSettingsRepositoryApi.DEFAULT_SETTINGS)
    override val settings: StateFlow<GlobalSettingsRepositoryApi.SettingsDb> = _settings

    init {
        load()
    }

    override fun setSettings(newSettings: GlobalSettingsRepositoryApi.SettingsDb) {
        save(newSettings)
        _settings.value = newSettings
    }

    private fun load() {
        Log.d(classTag, "load")
        _settings.value = try {
            GlobalSettingsRepositoryApi.SettingsDb(
                darkModeAuto = prefs.getBoolean(
                    DARK_MODE_AUTO,
                    GlobalSettingsRepositoryApi.DEFAULT_SETTINGS.darkModeAuto
                ),
                darkMode = prefs.getBoolean(DARK_MODE, GlobalSettingsRepositoryApi.DEFAULT_SETTINGS.darkMode),
                askToExitFromApp = prefs.getBoolean(
                    ASK_TO_EXT_FROM_APP,
                    GlobalSettingsRepositoryApi.DEFAULT_SETTINGS.askToExitFromApp
                ),
                keepScreenOn = prefs.getBoolean(
                    KEEP_SCREEN_ON,
                    GlobalSettingsRepositoryApi.DEFAULT_SETTINGS.keepScreenOn
                ),

            )
        } catch (ignored: Exception) {
            GlobalSettingsRepositoryApi.DEFAULT_SETTINGS
        }
    }

    private fun save(settings: GlobalSettingsRepositoryApi.SettingsDb) {
        Log.d(classTag, "save")
        prefs.edit(commit = true) {
            putBoolean(DARK_MODE_AUTO, settings.darkModeAuto)
            putBoolean(DARK_MODE, settings.darkMode)
            putBoolean(ASK_TO_EXT_FROM_APP, settings.askToExitFromApp)
            putBoolean(KEEP_SCREEN_ON, settings.keepScreenOn)
        }
    }

    companion object {
        private const val PREFS_NAME = "global_settings"
        private const val DARK_MODE_AUTO = "dark_mode_auto"
        private const val DARK_MODE = "dark_mode"
        private const val ASK_TO_EXT_FROM_APP = "ask_to_ext_from_app"
        private const val KEEP_SCREEN_ON = "keep_screen_on"
    }
}
