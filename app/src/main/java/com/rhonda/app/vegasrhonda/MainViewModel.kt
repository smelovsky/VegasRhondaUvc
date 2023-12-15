package com.rhonda

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rhonda.data.repository.api.GlobalSettingsRepositoryApi
import com.rhonda.app.vegasrhonda.permissions.PermissionsApi
import com.rhonda.app.vegasrhonda.permissions.PermissionsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

////////////////////////////////////////////////////////////////////////////////////////////////////

data class InfoSectionViewState(
    val cameraInfo: String,
) {
    companion object {
        val DEFAULT = InfoSectionViewState(
            cameraInfo = "",
        )
    }
}


////////////////////////////////////////////////////////////////////////////////////////////////////

data class SettingsViewState(
    val darkModeAutoSelected: Boolean,
    val darkModeLightSelected: Boolean,
    val darkModeDarkSelected: Boolean,
    val askToExitFromApp: Boolean,
    val keepScreenOn: Boolean,
)


@HiltViewModel
class MainViewModel @Inject constructor(
    private val permissionsApi: PermissionsApi,
    private val globalSettingsRepositoryApi: GlobalSettingsRepositoryApi,
    ) : ViewModel() {

    private val classTag = this::class.java.simpleName

    val exitFromApp = mutableStateOf(false)

    private val _settingsViewState = MutableStateFlow(mapSettingsToViewState(globalSettingsRepositoryApi.settings.value))
    val settingsViewState: StateFlow<SettingsViewState> = _settingsViewState

    val permissionsViewState = mutableStateOf(PermissionsViewState())

    var current_tab_index = 0

    private val _infoSectionState = MutableStateFlow(InfoSectionViewState.DEFAULT)
    val infoSectionState: StateFlow<InfoSectionViewState> = _infoSectionState


    init {

        viewModelScope.launch(Dispatchers.Default) {
            globalSettingsRepositoryApi.settings.collect {
                _settingsViewState.value = mapSettingsToViewState(it)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private fun mapSettingsToViewState(settings: GlobalSettingsRepositoryApi.SettingsDb): SettingsViewState {
        return SettingsViewState(
            darkModeAutoSelected = settings.darkModeAuto,
            darkModeLightSelected = !settings.darkModeAuto && !settings.darkMode,
            darkModeDarkSelected = !settings.darkModeAuto && settings.darkMode,
            askToExitFromApp = settings.askToExitFromApp,
            keepScreenOn = settings.keepScreenOn,
        )
    }

    fun onDarkModeAutoClicked() {
        viewModelScope.launch(Dispatchers.Default) {
            globalSettingsRepositoryApi.setSettings(
                globalSettingsRepositoryApi.settings.value.copy(
                    darkModeAuto = true,
                )
            )
        }
    }

    fun onDarkModeLightClicked() {
        viewModelScope.launch(Dispatchers.Default) {
            globalSettingsRepositoryApi.setSettings(
                globalSettingsRepositoryApi.settings.value.copy(
                    darkMode = false,
                    darkModeAuto = false,
                )
            )
        }
    }

    fun onDarkModeDarkClicked() {
        viewModelScope.launch(Dispatchers.Default) {
            globalSettingsRepositoryApi.setSettings(
                globalSettingsRepositoryApi.settings.value.copy(
                    darkMode = true,
                    darkModeAuto = false,
                )
            )
        }
    }

    fun onExitFromAppChecked(checked: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            globalSettingsRepositoryApi.setSettings(
                globalSettingsRepositoryApi.settings.value.copy(
                    askToExitFromApp = checked
                )
            )
        }
    }

    fun onKeepScreenOn(checked: Boolean) {
        viewModelScope.launch(Dispatchers.Default) {
            globalSettingsRepositoryApi.setSettings(
                globalSettingsRepositoryApi.settings.value.copy(
                    keepScreenOn = checked
                )
            )
        }
    }


    fun getPermissionsApi() : PermissionsApi {
        return permissionsApi
    }


}
