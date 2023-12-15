package com.rhonda.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.rhonda.R
import com.rhonda.ui.common.MainScaffold

@Composable
fun HomeRoute(
    navigateBack: () -> Unit,
    navigateToUvc: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val allowedNavigateBack = remember() { navigateBack }
    val allowedNavigateToUvc = remember() { navigateToUvc }
    val allowedNavigateToSettings = remember() {
        navigateToSettings
    }
    HomeScreen(
        navigateBack = { allowedNavigateBack() },
        navigateToUvc = { allowedNavigateToUvc() },
        navigateToSettings = { allowedNavigateToSettings() },
        modifier = modifier,
    )


}

@Composable
fun HomeScreen(
    navigateBack: () -> Unit,
    navigateToUvc: () -> Unit,
    navigateToSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {

    BackHandler(onBack = navigateBack)

    MainScaffold(
        titleRes = R.string.home_screen_title,
        modifier = modifier,
        navigateToUvc = navigateToUvc,
        navigateToSettings = navigateToSettings,
    ) { innerPadding ->

        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = painterResource(R.drawable.vegas_01),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().padding(innerPadding),
            )
        }

    }
}


