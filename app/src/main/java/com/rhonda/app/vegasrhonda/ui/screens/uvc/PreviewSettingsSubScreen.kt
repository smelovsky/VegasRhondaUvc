package com.rhonda.app.vegasrhonda.ui.screens.uvc

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rhonda.sdk.ui.compose.widgets.BasicDropDownList

@Composable
fun PreviewSettingsSubScreen(
    modifier: Modifier = Modifier,
    viewModel: UvcViewModel,
    onClose: () -> Unit,
) {
    val state by viewModel.previewSettingsState.collectAsState()
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(Modifier.height(10.dp))

            // info labels

            Row(
                modifier = Modifier.padding(6.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Checkbox(
                    checked = state.isShowInfoLabels,
                    onCheckedChange = { viewModel.onPreviewShowLabelsChecked(it) },
                )
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = "Show info labels",
                )
            }

            Spacer(Modifier.height(10.dp))

            // aspect ratio

            Row(
                modifier = Modifier.padding(6.dp),
                horizontalArrangement = Arrangement.Start,
            ) {
                Checkbox(
                    checked = state.isKeepAspectRatio,
                    onCheckedChange = { viewModel.onPreviewKeepAspectRatioChecked(it) },
                )
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = "Keep aspect ratio",
                )
            }

            Spacer(Modifier.height(20.dp))

            // preview format

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Resolution:")
                Spacer(Modifier.width(10.dp))
                BasicDropDownList(
                    modifier = Modifier.width(200.dp),
                    fontSize = 14.sp,
                    enabled = true,
                    items = state.availableResolutions,
                    selectedIndex = state.resolutionSelectedIndex,
                    onSelect = {
                        viewModel.onPreviewResolutionSelect(it)
                    },
                )
            }

            Spacer(Modifier.height(20.dp))

            // preview FPS

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("FPS:")
                Spacer(Modifier.width(10.dp))
                BasicDropDownList(
                    modifier = Modifier.width(150.dp),
                    fontSize = 14.sp,
                    enabled = true,
                    items = state.availableFps,
                    selectedIndex = state.fpsSelectedIndex,
                    onSelect = {
                        viewModel.onPreviewFpsSelect(it)
                    },
                )
            }

            Spacer(Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                OutlinedButton(onClick = { onClose() }) {
                    Text("Apply")
                }
            }
        }
    }
}

