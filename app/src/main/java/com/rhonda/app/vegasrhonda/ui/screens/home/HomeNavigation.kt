package com.rhonda.ui.home

import androidx.compose.material.SnackbarHostState
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.rhonda.ui.navigation.AppNavigationDestination

object HomeDestination : AppNavigationDestination {
    override val route = "home_route"
    override val destination = "home_destination"

    val fullRoute = "${route}"

    override fun routeTo() = "${route}"
}


fun NavGraphBuilder.homeScreen(
    navigateBack: () -> Unit,
    navigateToUvc: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    composable(
        route = HomeDestination.fullRoute,
    ) {

        HomeRoute(
            navigateBack = navigateBack,
            navigateToUvc = navigateToUvc,
            navigateToSettings = navigateToSettings,
        )
    }
}