/*
 * Copyright 2025 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.virtualgamepad.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.android.virtualgamepad.R
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.components.AdapterIndexConstants.COLOR_THEME_INDEX_DARK
import com.android.virtualgamepad.ui.components.AdapterIndexConstants.COLOR_THEME_INDEX_LIGHT
import com.android.virtualgamepad.ui.components.AdapterIndexConstants.GAMEPAD_STYLE_INDEX_INLINE
import com.android.virtualgamepad.ui.components.AdapterIndexConstants.GAMEPAD_STYLE_INDEX_INVERTED
import com.android.virtualgamepad.ui.components.AdapterIndexConstants.SIZE_INDEX_LARGE
import com.android.virtualgamepad.ui.components.AdapterIndexConstants.SIZE_INDEX_MEDIUM
import com.android.virtualgamepad.ui.components.AdapterIndexConstants.SIZE_INDEX_SMALL
import com.android.virtualgamepad.ui.theme.GamepadColorTheme
import com.android.virtualgamepad.ui.theme.GamepadSize
import com.android.virtualgamepad.ui.theme.GamepadStyle
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

internal object AdapterIndexConstants {
    const val COLOR_THEME_INDEX_LIGHT = 0
    const val COLOR_THEME_INDEX_DARK = 1

    const val SIZE_INDEX_SMALL = 0
    const val SIZE_INDEX_MEDIUM = 1
    const val SIZE_INDEX_LARGE = 2

    const val GAMEPAD_STYLE_INDEX_INLINE = 0
    const val GAMEPAD_STYLE_INDEX_INVERTED = 1
}

@Composable
fun FunctionButtons(
    onGamepadStyleChanged: (GamepadStyle) -> Unit,
    onGamepadSizeChanged: (GamepadSize) -> Unit,
    onGamepadColorThemeChanged: (GamepadColorTheme) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val theme = LocalGamepadTheme.current

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize(),
        ) {
            SettingsButton(
                modifier =
                    Modifier.width(theme.functionButtonWidth.dp)
                        .height(theme.functionButtonHeight.dp)
            ) {
                expanded = true
            }
            GameButton(
                GameButtonType.Menu,
                modifier =
                    Modifier.width(theme.functionButtonWidth.dp)
                        .height(theme.functionButtonHeight.dp),
            )
        }
        if (expanded) {
            GamepadSettingsDialog(
                onGamepadStyleChanged,
                onGamepadSizeChanged,
                onGamepadColorThemeChanged,
                { expanded = false },
            )
        }
    }
}

@Composable
fun GamepadSettingsDialog(
    onGamepadStyleChanged: (GamepadStyle) -> Unit,
    onGamepadSizeChanged: (GamepadSize) -> Unit,
    onGamepadColorThemeChanged: (GamepadColorTheme) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Row 1: Text
                Text(
                    text = stringResource(R.string.gamepad_style),
                    style = MaterialTheme.typography.titleMedium,
                )

                // Row 2: Dropdown Menu 1
                GamepadStyleSelector(onGamepadStyleChanged)
                GamepadSizeSelector(onGamepadSizeChanged)

                // Row 4: Text
                Text(
                    text = stringResource(R.string.gamepad_theme),
                    style = MaterialTheme.typography.bodyMedium,
                )

                GamepadColorThemeSelector(onGamepadColorThemeChanged)
            }
        }
    }
}

@Composable
fun GamepadStyleSelector(onGamepadStyleSelected: (GamepadStyle) -> Unit) {
    GamepadDropdownMenu(
        onItemSelected = { index ->
            when (index) {
                GAMEPAD_STYLE_INDEX_INLINE -> onGamepadStyleSelected(GamepadStyle.INLINE)
                GAMEPAD_STYLE_INDEX_INVERTED -> onGamepadStyleSelected(GamepadStyle.INVERTED)
                else -> onGamepadStyleSelected(GamepadStyle.INLINE)
            }
        },
        label = stringResource(R.string.label_gamepad_style),
        items = stringArrayResource(R.array.gamepad_style_items).toList(),
    )
}

@Composable
fun GamepadSizeSelector(onGamepadSizeSelected: (GamepadSize) -> Unit) {
    GamepadDropdownMenu(
        onItemSelected = { index ->
            val size =
                when (index) {
                    SIZE_INDEX_SMALL -> GamepadSize.SMALL
                    SIZE_INDEX_MEDIUM -> GamepadSize.MEDIUM
                    SIZE_INDEX_LARGE -> GamepadSize.LARGE
                    else -> GamepadSize.MEDIUM
                }
            onGamepadSizeSelected(size)
        },
        label = stringResource(R.string.label_gamepad_size),
        items = stringArrayResource(R.array.gamepad_size_items).toList(),
    )
}

@Composable
fun GamepadColorThemeSelector(onGamepadColorThemeSelected: (GamepadColorTheme) -> Unit) {
    ThemeSelectionRow(
        onOptionSelected = { index ->
            val colorTheme =
                when (index) {
                    COLOR_THEME_INDEX_LIGHT -> GamepadColorTheme.LIGHT
                    COLOR_THEME_INDEX_DARK -> GamepadColorTheme.DARK
                    else -> GamepadColorTheme.LIGHT
                }
            onGamepadColorThemeSelected(colorTheme)
        },
        options = stringArrayResource(R.array.gamepad_theme_items).toList(),
    )
}

@Composable
fun GamepadDropdownMenu(onItemSelected: (Int) -> Unit, label: String, items: List<String>) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(0) }
    val selectedText = items.getOrNull(selectedIndex) ?: ""

    Box {
        // Clickable element to show selection and toggle menu
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "$label: $selectedText",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
            )
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Dropdown Arrow")
        }

        // Stable DropdownMenu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(),
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onItemSelected(index)
                    },
                )
            }
        }
    }
}

@Composable
fun ThemeSelectionRow(onOptionSelected: (Int) -> Unit, options: List<String>) {
    val selectedOption = remember { mutableStateOf(options.firstOrNull()) }

    Row(
        modifier = Modifier.selectableGroup().fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        options.forEachIndexed { index, text ->
            Row(
                modifier =
                    Modifier.selectable(
                            selected = (text == selectedOption.value),
                            onClick = {
                                onOptionSelected(index)
                                selectedOption.value = text
                            },
                            role = Role.RadioButton,
                        )
                        .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = (text == selectedOption.value),
                    onClick = null, // Handled by the Row's selectable
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 70)
@Composable
fun FunctionButtonsPreview() {
    VirtualGamepadTheme { FunctionButtons({ _ -> }, { _ -> }, { _ -> }) }
}
