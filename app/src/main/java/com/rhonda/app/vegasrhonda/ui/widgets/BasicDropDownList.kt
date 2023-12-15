/*
 * Copyright (C) 2021 Rhonda Software.
 * All rights reserved.
 */

package com.rhonda.sdk.ui.compose.widgets

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize

@Composable
fun <T> BasicDropDownList(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp,
    enabled: Boolean = true,
    items: List<T>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    var expanded by remember(items, selectedIndex) { mutableStateOf(false) }
    var selectedText by remember(items, selectedIndex) { mutableStateOf("${items.getOrNull(selectedIndex) ?: ""}") }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (expanded)
        Icons.Filled.ArrowDropUp
    else
        Icons.Filled.ArrowDropDown

    Column {

        BasicTextField(
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colors.onSurface.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                .padding(horizontal = 6.dp, vertical = 3.dp)
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                },
            enabled = enabled,
            readOnly = true,
            maxLines = 1,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colors.onSurface.copy(alpha = if (enabled) 1f else 0.5f),
                fontSize = fontSize
            ),
            decorationBox = { innerTextField ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(Modifier.width(with(LocalDensity.current) {
                        (textFieldSize.width.toDp() - 24.dp).coerceAtLeast(0.dp)
                    })) {
                        innerTextField()
                    }
                    Spacer(Modifier.weight(1f))
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { if (enabled) expanded = !expanded }
                    )
                }
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textFieldSize.width.toDp() })
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(onClick = {
                    onSelect(index)
                    selectedText = "$item"
                    expanded = false
                }) {
                    Text(
                        text = "$item",
                        fontSize = fontSize,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}
