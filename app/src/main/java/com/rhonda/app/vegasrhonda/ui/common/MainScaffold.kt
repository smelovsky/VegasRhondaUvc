package com.rhonda.ui.common

import android.app.Activity
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.Web
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.Bluetooth
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Usb
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material.icons.outlined.Web
import androidx.compose.material.icons.outlined.Wifi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.rhonda.R
import com.rhonda.app.vegasrhonda.mainViewModel
import kotlinx.coroutines.launch

data class NavigationItem(
    var id: Int,
    val isSelected: Boolean,
    var label: String,
    val iconFilled: ImageVector,
    val iconsOutlined: ImageVector,
    val navigateTo: (() -> Unit)?
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    @StringRes titleRes: Int,
    modifier: Modifier = Modifier,
    navigateToHome: (() -> Unit)? = null,
    navigateToUvc: (() -> Unit)? = null,
    navigateToSettings: (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    androidx.compose.material3.Scaffold(
        modifier = modifier,
        topBar = {
            MainTopAppBar(
                titleRes = titleRes,
            )
        },
        bottomBar = {
            MainBottomAppBar(
                navigateToHome = navigateToHome,
                navigateToUvc = navigateToUvc,
                navigateToSettings = navigateToSettings,
            )
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    @StringRes titleRes: Int,
) {
    androidx.compose.material.TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                androidx.compose.material.Text(stringResource(id = titleRes))

            }
        },
        elevation = 0.dp,
        //navigationIcon = { },
        actions = {
            androidx.compose.material.IconButton(onClick = {
                mainViewModel.exitFromApp.value = true
            }) {
                androidx.compose.material.Icon(
                    Icons.Outlined.ExitToApp,
                    contentDescription = "Exit",
                )
            }
        }
    )
}


@Composable
fun MainBottomAppBar(
    navigateToHome: (() -> Unit)? = null,
    navigateToUvc: (() -> Unit)? = null,
    navigateToSettings: (() -> Unit)? = null,
) {
    val lazyListState: LazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    var settingsList = listOf<NavigationItem>(
        NavigationItem(0, navigateToHome == null, stringResource(R.string.home_screen_title), Icons.Filled.Home, Icons.Outlined.Home, navigateToHome),
        NavigationItem(1, navigateToUvc == null, stringResource(R.string.uvc_screen_title), Icons.Filled.Usb, Icons.Outlined.Usb, navigateToUvc),
        NavigationItem(2, navigateToSettings == null, stringResource(R.string.settings_screen_title), Icons.Filled.Settings, Icons.Outlined.Settings, navigateToSettings),
    )

    LaunchedEffect(Unit) {

        var isItemVisible: Boolean = false

        with(lazyListState.layoutInfo) {
            val itemVisibleInfo = visibleItemsInfo.find { it.index == mainViewModel.current_tab_index }
            isItemVisible = if (itemVisibleInfo == null) {
                false
            } else {
                viewportEndOffset - itemVisibleInfo.offset >= itemVisibleInfo.size
            }
        }

        if (!isItemVisible) {
            coroutineScope.launch {
                lazyListState.scrollToItem(mainViewModel.current_tab_index)
            }
        }

    }

    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background,
        contentColor = MaterialTheme.colors.primary,
        elevation = 0.dp,
    ) {
        LazyRow (
            modifier = Modifier
                .fillMaxWidth(),
            state = lazyListState,
            horizontalArrangement = Arrangement.Center,
        ) {
            itemsIndexed(
                items = settingsList,
                key = { _, settingsItem -> settingsItem.id })
            { index, item ->

                val icon = if (item.isSelected) item.iconFilled else item.iconsOutlined

                BottomNavigationItem(
                    selected = true,
                    onClick = {
                        if (item.navigateTo != null) {
                            item.navigateTo.invoke()
                            mainViewModel.current_tab_index = index
                        }
                    },
                    label = {
                        androidx.compose.material.Text(item.label)
                    },
                    icon = {
                        androidx.compose.material.Icon(icon, contentDescription = null)
                    },
                )
            }
        }
    }

}



