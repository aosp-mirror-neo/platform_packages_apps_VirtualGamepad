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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.android.virtualgamepad.R
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.GamepadLayoutSpec
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.components.StringResourceAdapter.colorThemeIndex
import com.android.virtualgamepad.ui.components.StringResourceAdapter.gamepadSizeIndex
import com.android.virtualgamepad.ui.components.StringResourceAdapter.gamepadStyleIndex
import com.android.virtualgamepad.ui.theme.GamepadColorTheme
import com.android.virtualgamepad.ui.theme.GamepadSize
import com.android.virtualgamepad.ui.theme.GamepadStyle
import com.android.virtualgamepad.ui.theme.GamepadTheme
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

private const val DIALOG_STATE_COLLAPSED = 0
private const val DIALOG_STATE_SETTINGS = 1
private const val DIALOG_STATE_STYLE = 2
private const val DIALOG_STATE_HIDE_CONFIRM = 3

@Composable
fun FunctionButtons(
    modifier: Modifier = Modifier,
    onGamepadStyleChanged: (GamepadStyle) -> Unit,
    onGamepadHapticsChanged: (Boolean) -> Unit,
    onGamepadSizeChanged: (GamepadSize) -> Unit,
    onGamepadColorThemeChanged: (GamepadColorTheme) -> Unit,
    onGamepadDismissed: (Boolean) -> Unit,
) {
    var dialogState by remember { mutableStateOf(DIALOG_STATE_COLLAPSED) }
    val gamepadTheme = LocalGamepadTheme.current
    val gamepadLayoutSpec = GamepadLayoutSpec(gamepadTheme)

    val functionButtonWidth = gamepadLayoutSpec.getFunctionButtonWidth()
    val functionButtonHeight = gamepadLayoutSpec.getFunctionButtonHeight()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.wrapContentSize(),
    ) {
        SettingsButton(
            modifier = Modifier.width(functionButtonWidth.dp).height(functionButtonHeight.dp)
        ) {
            dialogState = DIALOG_STATE_SETTINGS
        }
        Spacer(modifier = Modifier.width(128.dp))
        GameButton(
            GameButtonType.Menu,
            modifier = Modifier.width(functionButtonWidth.dp).height(functionButtonHeight.dp),
        )
    }
    when (dialogState) {
        DIALOG_STATE_SETTINGS -> {
            GamepadSettingsDialog(
                gamepadTheme = gamepadTheme,
                onGamepadHideRequest = { dialogState = DIALOG_STATE_HIDE_CONFIRM },
                onDismissRequest = { dialogState = DIALOG_STATE_COLLAPSED },
                onGamepadStyleRequest = { dialogState = DIALOG_STATE_STYLE },
                onGamepadHapticsChanged = onGamepadHapticsChanged,
            )
        }

        DIALOG_STATE_STYLE -> {
            GamepadStyleDialog(
                gamepadTheme = gamepadTheme,
                onGamepadStyleChanged = onGamepadStyleChanged,
                onGamepadSizeChanged = onGamepadSizeChanged,
                onGamepadColorThemeChanged = onGamepadColorThemeChanged,
                onDismissRequest = { dialogState = DIALOG_STATE_COLLAPSED },
            )
        }

        DIALOG_STATE_HIDE_CONFIRM -> {
            GamepadHideConfirmDialog(
                gamepadTheme = gamepadTheme,
                onGamepadDismissed = onGamepadDismissed,
                onDismissRequest = { dialogState = DIALOG_STATE_COLLAPSED },
            )
        }
    }
}

@Composable
fun GamepadStyleDialog(
    gamepadTheme: GamepadTheme,
    onGamepadStyleChanged: (GamepadStyle) -> Unit,
    onGamepadSizeChanged: (GamepadSize) -> Unit,
    onGamepadColorThemeChanged: (GamepadColorTheme) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.width(364.dp).wrapContentHeight().padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = gamepadTheme.dialogColor()),
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Row 1: Text
                DialogLineHeader(
                    text = stringResource(R.string.gamepad_style),
                    gamepadTheme = gamepadTheme,
                )

                // Row 2: Dropdown Menu 1
                GamepadStyleSelector(gamepadTheme, onGamepadStyleChanged)
                GamepadSizeSelector(gamepadTheme, onGamepadSizeChanged)

                // Row 4: Text
                DialogLineHeader(
                    text = stringResource(R.string.gamepad_theme),
                    gamepadTheme = gamepadTheme,
                )

                GamepadColorThemeSelector(gamepadTheme, onGamepadColorThemeChanged)
            }
        }
    }
}

@Composable
fun GamepadSettingsDialog(
    gamepadTheme: GamepadTheme,
    onGamepadStyleRequest: () -> Unit,
    onGamepadHideRequest: () -> Unit,
    onDismissRequest: () -> Unit,
    onGamepadHapticsChanged: (Boolean) -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.width(364.dp).wrapContentHeight().padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = gamepadTheme.dialogColor()),
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Row 1: Gamepad style button
                Row(
                    modifier =
                        Modifier.padding(top = 8.dp, bottom = 8.dp).clickable {
                            onGamepadStyleRequest()
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        imageVector = Icons.Filled.SportsEsports,
                        contentDescription = null,
                        tint = gamepadTheme.dialogIconTint(),
                    )
                    Column() {
                        DialogTitle(
                            text = stringResource(R.string.gamepad_style),
                            gamepadTheme = gamepadTheme,
                        )
                        val styleIndex = gamepadStyleIndex(LocalGamepadTheme.current.style)
                        val style = stringArrayResource(R.array.gamepad_style_items).get(styleIndex)
                        val sizeIndex = gamepadSizeIndex(LocalGamepadTheme.current.size)
                        val size = stringArrayResource(R.array.gamepad_size_items).get(sizeIndex)
                        DialogSubtitle(
                            text =
                                stringResource(R.string.template_gamepad_style_text, style, size),
                            gamepadTheme = gamepadTheme,
                        )
                    }
                }

                // Row 2: Haptics
                val hapticsStateDescription =
                    stringResource(
                        if (gamepadTheme.isHapticsEnabled) R.string.haptics_on
                        else R.string.haptics_off
                    )
                Row(
                    modifier =
                        Modifier.fillMaxWidth()
                            .toggleable(
                                value = gamepadTheme.isHapticsEnabled,
                                onValueChange = onGamepadHapticsChanged,
                            )
                            .semantics(mergeDescendants = true) {
                                stateDescription = hapticsStateDescription
                            }
                            .padding(top = 8.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                            imageVector = Icons.Filled.Vibration,
                            contentDescription = null,
                            tint = gamepadTheme.dialogIconTint(),
                        )
                        DialogTitle(
                            text = stringResource(R.string.haptics),
                            gamepadTheme = gamepadTheme,
                        )
                    }

                    Switch(
                        checked = gamepadTheme.isHapticsEnabled,
                        onCheckedChange = null,
                        enabled = true,
                        thumbContent =
                            if (gamepadTheme.isHapticsEnabled) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize),
                                    )
                                }
                            } else {
                                null // No icon when unchecked
                            },
                    )
                }

                // Row 3: Dismiss Gamepad button
                Row(
                    modifier =
                        Modifier.padding(top = 8.dp, bottom = 8.dp).clickable {
                            onGamepadHideRequest()
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = gamepadTheme.dialogIconTint(),
                    )
                    DialogTitle(
                        text = stringResource(R.string.hide_gamepad),
                        gamepadTheme = gamepadTheme,
                    )
                }
            }
        }
    }
}

@Composable
fun GamepadHideConfirmDialog(
    gamepadTheme: GamepadTheme,
    onGamepadDismissed: (Boolean) -> Unit,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier.width(364.dp).wrapContentHeight().padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(containerColor = gamepadTheme.dialogColor()),
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                DialogTitle(
                    text = stringResource(R.string.hide_gamepad_title),
                    gamepadTheme = gamepadTheme,
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    // Highlighted option: Hide for now
                    Button(
                        onClick = { onGamepadDismissed(false) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                    ) {
                        Text(text = stringResource(R.string.hide_for_now))
                    }

                    // Secondary option: Always hide
                    OutlinedButton(
                        onClick = { onGamepadDismissed(true) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(28.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.always_hide),
                            style = gamepadTheme.dialogBodyStyle(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun DialogLineHeader(gamepadTheme: GamepadTheme, text: String) {
    Text(text = text, style = gamepadTheme.dialogHeaderStyle())
}

@Composable
internal fun DialogTitle(gamepadTheme: GamepadTheme, text: String) {
    Text(text = text, style = gamepadTheme.dialogTitleStyle())
}

@Composable
internal fun DialogSubtitle(gamepadTheme: GamepadTheme, text: String) {
    Text(text = text, style = gamepadTheme.dialogSubtitleStyle())
}

@Composable
fun GamepadStyleSelector(
    gamepadTheme: GamepadTheme,
    onGamepadStyleSelected: (GamepadStyle) -> Unit,
) {
    GamepadDropdownMenu(
        gamepadTheme = gamepadTheme,
        onItemSelected = { index ->
            val style = StringResourceAdapter.gamepadStyleFromIndex(index)
            onGamepadStyleSelected(style)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.SportsEsports,
                contentDescription = null,
                tint = gamepadTheme.dropdownItemTint(),
            )
        },
        description = stringResource(R.string.description_gamepad_style_selector),
        items = stringArrayResource(R.array.gamepad_style_items).toList(),
        selectionIndex = gamepadStyleIndex(LocalGamepadTheme.current.style),
    )
}

@Composable
fun GamepadSizeSelector(gamepadTheme: GamepadTheme, onGamepadSizeSelected: (GamepadSize) -> Unit) {
    GamepadDropdownMenu(
        gamepadTheme = gamepadTheme,
        onItemSelected = { index ->
            val size = StringResourceAdapter.gamepadSizeFromIndex(index)
            onGamepadSizeSelected(size)
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Straighten,
                contentDescription = null,
                tint = gamepadTheme.dropdownItemTint(),
            )
        },
        description = stringResource(R.string.description_gamepad_size_selector),
        items = stringArrayResource(R.array.gamepad_size_items).toList(),
        selectionIndex = gamepadSizeIndex(LocalGamepadTheme.current.size),
    )
}

@Composable
fun GamepadColorThemeSelector(
    gamepadTheme: GamepadTheme,
    onGamepadColorThemeSelected: (GamepadColorTheme) -> Unit,
) {
    ThemeSelectionRow(
        gamepadTheme = gamepadTheme,
        onOptionSelected = { index ->
            val colorTheme = StringResourceAdapter.colorThemeFromIndex(index)
            onGamepadColorThemeSelected(colorTheme)
        },
        options = stringArrayResource(R.array.gamepad_theme_items).toList(),
        selectionIndex = colorThemeIndex(LocalGamepadTheme.current.colorTheme),
    )
}

@Composable
fun GamepadDropdownMenu(
    gamepadTheme: GamepadTheme,
    onItemSelected: (Int) -> Unit,
    description: String,
    items: List<String>,
    selectionIndex: Int,
    leadingIcon: @Composable () -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(selectionIndex) }
    val selectedText = items.getOrNull(selectedIndex) ?: ""

    Box {
        // Clickable element to show selection and toggle menu
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
            ) {
                Box(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) { leadingIcon() }
                Text(
                    text = selectedText,
                    modifier = Modifier.weight(1f),
                    style = gamepadTheme.dropDownItemStyle(),
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = description,
                    tint = gamepadTheme.dialogIconTint(),
                )
            }
        }

        // Stable DropdownMenu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = gamepadTheme.dropdownColor(),
            shape = MaterialTheme.shapes.large,
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = { Text(text = item, style = gamepadTheme.dropDownItemStyle()) },
                    leadingIcon = leadingIcon,
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
fun ThemeSelectionRow(
    gamepadTheme: GamepadTheme,
    onOptionSelected: (Int) -> Unit,
    options: List<String>,
    selectionIndex: Int,
) {
    val selectedOption = remember { mutableStateOf(options[selectionIndex]) }

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
                    style = gamepadTheme.dialogHeaderStyle(),
                    modifier = Modifier.padding(start = 4.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 70)
@Composable
fun FunctionButtonsPreview() {
    VirtualGamepadTheme {
        FunctionButtons(
            Modifier.width(300.dp).height(70.dp),
            { _ -> },
            { _ -> },
            { _ -> },
            { _ -> },
            {},
        )
    }
}
