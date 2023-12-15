package com.rhonda.ui.settings


import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rhonda.R
import com.rhonda.app.vegasrhonda.mainViewModel
import com.rhonda.ui.common.MainScaffold

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.HelpOutline
import androidx.compose.material.icons.rounded.LightMode

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.structuralEqualityPolicy

import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp



@Composable
fun SettingsRoute(
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToUvc: () -> Unit,
    modifier: Modifier = Modifier

    ) {


    val allowedNavigateBack = remember() { navigateBack }
    val allowedNavigateToHome = remember() { navigateToHome }
    val allowedNavigateToUvc = remember() { navigateToUvc }

    SettingsScreen(
        modifier = modifier,
        navigateBack = {  allowedNavigateBack() },
        navigateToHome = { allowedNavigateToHome() },
        navigateToUvc = { allowedNavigateToUvc() },
    )

}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToUvc: () -> Unit,
) {

    BackHandler(onBack = navigateBack)

    MainScaffold(
        titleRes = R.string.settings_screen_title,
        navigateToHome = navigateToHome,
        navigateToUvc = navigateToUvc,
    ) { innerPadding ->

        val scrollState = rememberScrollState()
        val settingsViewState = mainViewModel.settingsViewState.collectAsState()

        Box(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
            .background(Color.Transparent)
            ,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .verticalScroll(scrollState)
                ,
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(R.string.dark_mode))
                    Spacer(Modifier.weight(1f))

                    SelectableItem(
                        selected = settingsViewState.value.darkModeAutoSelected,
                        onClick = { mainViewModel.onDarkModeAutoClicked() },
                    ) {
                        Text(
                            modifier = Modifier.padding(
                                horizontal = 4.dp
                            ), text = stringResource(R.string.auto)
                        )
                    }
                    SelectableItem(
                        selected = settingsViewState.value.darkModeLightSelected,
                        onClick = { mainViewModel.onDarkModeLightClicked() },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.LightMode,
                            contentDescription = ""
                        )
                    }
                    SelectableItem(
                        selected = settingsViewState.value.darkModeDarkSelected,
                        onClick = { mainViewModel.onDarkModeDarkClicked() },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.DarkMode,
                            contentDescription = ""
                        )
                    }
                }

                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(stringResource(R.string.ask_confirmation_to_exit_from_app))

                    }
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = settingsViewState.value.askToExitFromApp,
                        onCheckedChange = { mainViewModel.onExitFromAppChecked(it) },
                    )
                }

                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(stringResource(R.string.keep_screen_on))

                    }
                    Spacer(Modifier.weight(1f))
                    Switch(
                        checked = settingsViewState.value.keepScreenOn,
                        onCheckedChange = { mainViewModel.onKeepScreenOn(it) },
                    )
                }
            }
        }

    }

}

@Composable
private fun SelectableItem(
    modifier: Modifier = Modifier,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(percent = 50)
    Surface(
        modifier = modifier
            .padding(horizontal = 4.dp)
            .clip(shape)
            .clickable { onClick() },
        shape = shape,
        color = if (selected) MaterialTheme.colors.secondary.copy(alpha = 0.2f) else Color.Transparent,
        border = BorderStroke(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.1f)),
    ) {
        Box(
            modifier = Modifier.padding(4.dp),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
