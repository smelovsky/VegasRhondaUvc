package com.rhonda.ui.permissions

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.rhonda.R
import com.rhonda.app.vegasrhonda.mainViewModel


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun PermissionsScreen(
    modifier: Modifier = Modifier,
    activity: Activity,
) {

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        androidx.compose.material3.Scaffold(
            modifier = modifier,

            topBar = {
                androidx.compose.material.TopAppBar(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.primary,
                    title = {
                        Row() {
                            Text(
                                text = stringResource(R.string.app_name),
                            )
                        }

                    },
                    modifier = Modifier.height(30.dp),
                    actions = {
                        androidx.compose.material.IconButton(onClick = {
                            //mainViewModel.onExitFromApp()
                            mainViewModel.exitFromApp.value = true
                        }) {
                            androidx.compose.material.Icon(
                                Icons.Outlined.ExitToApp,
                                contentDescription = "Exit",
                            )
                        }
                    }
                )
            },
            bottomBar = {
                androidx.compose.material3.OutlinedButton(
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colors.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    onClick = {

                        if (!mainViewModel.getPermissionsApi().hasBasePermissions(activity)) {
                            mainViewModel.getPermissionsApi().requestBasePermissions(activity)
                        }

                    }
                ) {
                    androidx.compose.material3.Text(
                        color = MaterialTheme.colors.primary,
                        text = "Permissions"
                    )
                }
            }

        ) {

            Image(
                painter = painterResource(R.drawable.vegas_01),
                contentDescription = "",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().padding(it),
            )

            Column(modifier = Modifier.fillMaxSize().padding(it)) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "INTERNET",
                    color = if (mainViewModel.permissionsViewState.value.INTERNET) Color.Green else Color.Red,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

                Text(
                    text = "CAMERA",
                    color = if (mainViewModel.permissionsViewState.value.CAMERA) Color.Green else Color.Red,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
                Text(
                    text = "WAKE_LOCK",
                    color = if (mainViewModel.permissionsViewState.value.WAKE_LOCK) Color.Green else Color.Red,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )

            }
        }
    }

}