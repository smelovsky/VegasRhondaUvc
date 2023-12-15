package com.rhonda.ui.navigation


import android.app.Activity
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rhonda.app.vegasrhonda.ui.screens.uvc.UvcDestination
import com.rhonda.app.vegasrhonda.ui.screens.uvc.uvcScreen
import com.rhonda.ui.home.HomeDestination
import com.rhonda.ui.home.HomeRoute
import com.rhonda.ui.home.homeScreen
import com.rhonda.ui.permissions.PermissionsDestination
import com.rhonda.ui.permissions.PermissionsRoute
import com.rhonda.ui.settings.SettingsDestination
import com.rhonda.ui.settings.settingsScreen


@ExperimentalComposeUiApi
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = HomeDestination.route,
    activity: Activity,
    onBackPressed: () -> Unit,
) {

    val navigateBack: () -> Unit = {
        onBackPressed()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = PermissionsDestination.route) {
            PermissionsRoute(
                activity = activity,
            )
        }
        composable(route = HomeDestination.route) {
            HomeRoute(
                navigateBack = navigateBack,
                navigateToUvc = { navController.switchTo(UvcDestination) },
                navigateToSettings = { navController.switchTo(SettingsDestination) },
            )
        }
        homeScreen(
            navigateBack = navigateBack,
            navigateToUvc = { navController.switchTo(UvcDestination) },
            navigateToSettings = { navController.switchTo(SettingsDestination) },
        )

        uvcScreen(
            navigateBack = navigateBack,
            navigateToHome = { navController.switchTo(HomeDestination) },
            navigateToSettings = { navController.switchTo(SettingsDestination) },
        )
        settingsScreen(
            navigateBack = navigateBack,
            navigateToHome = { navController.switchTo(HomeDestination) },
            navigateToUvc = { navController.switchTo(UvcDestination) },

        )
    }
}

fun NavController.switchTo(
    destination: AppNavigationDestination,
) = navigate(route = destination.routeTo())

