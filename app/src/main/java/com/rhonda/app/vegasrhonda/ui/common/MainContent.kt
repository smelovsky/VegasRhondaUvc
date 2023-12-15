package com.rhonda.ui

import android.app.Activity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.rhonda.app.vegasrhonda.mainViewModel
import com.rhonda.app.vegasrhonda.ui.theme.AppTheme
import com.rhonda.ui.home.HomeDestination
import com.rhonda.ui.navigation.AppNavHost
import com.rhonda.ui.permissions.PermissionsDestination
import com.rhonda.ui.theme.HidingSystemBarsController

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    darkMode: Boolean,
    activity: Activity,
    onBackPressed: () -> Unit,
) {
    AppTheme(
        darkTheme = darkMode,
        fullscreenMode = HidingSystemBarsController.fullscreenMode,
        ) {

        ProvideWindowInsets {

            val navController = rememberNavController()

            AppNavHost(
                navController = navController,
                modifier = modifier.systemBarsPadding(),
                startDestination = if (mainViewModel.permissionsViewState.value.permissionsGranted) HomeDestination.route else PermissionsDestination.route,
                activity = activity,
                onBackPressed = onBackPressed,
            )
        }
    }
}
