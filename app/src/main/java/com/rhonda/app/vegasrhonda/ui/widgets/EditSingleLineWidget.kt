/*
 * Copyright (C) 2021 Rhonda Software.
 * All rights reserved.
 */

package com.rhonda.sdk.ui.compose.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.ContentPaste
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.rhonda.R

@Composable
fun EditSingleLineWidget(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    label: String,
    text: String,
    isError: Boolean = false,
    isClipboardControls: Boolean = false,
    onTextChanged: (String) -> Unit,
    onFocusChanged: (FocusState) -> Unit = {},
    onDone: () -> Unit = {},
) {
    val clipboardManager = LocalClipboardManager.current
    var value by remember(text){ mutableStateOf(text)}
    val focusManager = LocalFocusManager.current
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier,
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { onFocusChanged(it) },
            readOnly = readOnly,
            enabled = enabled,
            isError = isError,
            maxLines = 1,
            singleLine = true,
            visualTransformation =
            if (keyboardType != KeyboardType.Password) VisualTransformation.None else {
                if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
            },
            value = value,
            onValueChange = { inputText ->
                onTextChanged(inputText)
                value = inputText
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done,

                ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onDone()
                    focusManager.clearFocus(true)
                }
            ),
            trailingIcon = {
                if (keyboardType == KeyboardType.Password) {
                    val image = if (passwordVisible)
                        ImageVector.vectorResource(R.drawable.baseline_visibility_24)
                    else  ImageVector.vectorResource(R.drawable.baseline_visibility_off_24)

                    // Please provide localized description for accessibility services
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                        Icon(imageVector  = image, description)
                    }
                }

            },
        )

        if (isClipboardControls) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                // copy
                IconButton(
                    enabled = enabled,
                    onClick = {
                        clipboardManager.setText(AnnotatedString(text))
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    )
                }

                // paste
                IconButton(
                    enabled = enabled,
                    onClick = {
                        clipboardManager.getText()?.let { annotatedString ->
                            onTextChanged(annotatedString.text)
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ContentPaste,
                        contentDescription = "",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                    )
                }
            }
        }
    }
}
