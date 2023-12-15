package com.rhonda.app.vegasrhonda.ui.screens.uvc

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.rhonda.ui.navigation.AppNavigationDestination

object UvcDestination : AppNavigationDestination {
    override val route = "uvc_route"
    override val destination = "uvc_destination"

    val fullRoute = "$route"

    override fun routeTo() = "$route"
}

fun NavGraphBuilder.uvcScreen(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToSettings: () -> Unit,
) {
    composable(
        route = UvcDestination.fullRoute,
    ) {

        UvcRoute(
            navigateBack = navigateBack,
            navigateToHome = navigateToHome,
            navigateToSettings = navigateToSettings,
        )
    }
}