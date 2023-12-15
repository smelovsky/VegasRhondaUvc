package com.rhonda.ui.settings

import androidx.compose.material.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rhonda.ui.navigation.AppNavigationDestination


object SettingsDestination : AppNavigationDestination {
    override val route = "settings_route"
    override val destination = "settings_destination"

    val fullRoute = "${route}"

    override fun routeTo() = "${route}"
}


fun NavGraphBuilder.settingsScreen(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToUvc: () -> Unit,
) {
    composable(
        route = SettingsDestination.fullRoute,

    ) {

        SettingsRoute(
            navigateBack = navigateBack,
            navigateToHome = navigateToHome,
            navigateToUvc = navigateToUvc,
        )
    }
}