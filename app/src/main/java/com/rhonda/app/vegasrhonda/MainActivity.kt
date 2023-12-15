package com.rhonda.app.vegasrhonda

import android.content.DialogInterface
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import com.rhonda.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.rhonda.R
import com.rhonda.app.vegasrhonda.ui.screens.uvc.UvcViewModel
import com.rhonda.ui.MainContent
import dagger.hilt.android.AndroidEntryPoint

sealed class AppFunction(var run: () -> Unit) {

    object exit : AppFunction( {} )
}


lateinit var mainViewModel: MainViewModel
lateinit var uvcViewModel: UvcViewModel

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    var isAppInited: Boolean = false

    @OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppFunction.exit.run = ::exitFromApp

        setContent {

            mainViewModel = hiltViewModel()
            uvcViewModel = hiltViewModel()

            val settingsViewState = mainViewModel.settingsViewState.collectAsState()

            if (mainViewModel.exitFromApp.value) {
                exitFromApp()
            }

            if (settingsViewState.value.keepScreenOn) {
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }

            val darkMode =
                if (settingsViewState.value.darkModeAutoSelected) isSystemInDarkTheme()
                else if (settingsViewState.value.darkModeDarkSelected) true else false

            mainViewModel.getPermissionsApi().hasAllPermissions(this)

            isAppInited = true

            MainContent(
                darkMode = darkMode,
                activity = this,
                onBackPressed = ::exitFromApponOnBackPressed,
            )
        }
    }

    override fun onBackPressed() {

        exitFromApponOnBackPressed()
    }

    fun exitFromApponOnBackPressed() {

        if (mainViewModel.settingsViewState.value.askToExitFromApp) {

            val alertDialog = android.app.AlertDialog.Builder(this)

            alertDialog.apply {
                setIcon(R.drawable.vegas_02)
                setTitle(getApplicationContext().getResources().getString(R.string.app_name))
                setMessage(
                    getApplicationContext().getResources()
                        .getString(R.string.do_you_really_want_to_close_the_application)
                )
                setPositiveButton(getApplicationContext().getResources().getString(R.string.yes))
                { _: DialogInterface?, _: Int -> exitFromApp() }
                setNegativeButton(getApplicationContext().getResources().getString(R.string.no))
                { _, _ -> }

            }.create().show()
        } else {
            exitFromApp()
        }
    }

    fun exitFromApp() {
        this.finish()
    }

    override fun onStart() {
        super.onStart()
        if (isAppInited) {

        }
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (isAppInited) {
            mainViewModel.getPermissionsApi().hasAllPermissions(this)

        }
    }


}



