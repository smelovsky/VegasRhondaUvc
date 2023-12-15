package com.rhonda.ui.permissions

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.rhonda.ui.navigation.AppNavigationDestination

object PermissionsDestination : AppNavigationDestination {
    override val route = "permissions_route"
    override val destination = "permissions_destination"
}

@ExperimentalComposeUiApi
@Composable
fun PermissionsRoute(
    modifier: Modifier = Modifier,
    activity: Activity
) {
    PermissionsScreen(
        modifier = modifier,
        activity = activity,
    )
}