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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.R
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.theme.GamepadSize
import com.android.virtualgamepad.ui.theme.GamepadSize.LARGE
import com.android.virtualgamepad.ui.theme.GamepadSize.MEDIUM
import com.android.virtualgamepad.ui.theme.GamepadSize.RETRO
import com.android.virtualgamepad.ui.theme.GamepadSize.SMALL
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

@Composable
fun FunctionButtons(themeChangedCallback: (GamepadSize) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val theme = LocalGamepadTheme.current

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize(),
        ) {
            RoundedCornerGameButton(
                GameButtonType.Select,
                modifier =
                    Modifier.width(theme.functionButtonWidth.dp)
                        .height(theme.functionButtonHeight.dp),
            )
            CircleGameButton(
                GameButtonType.Mode,
                modifier = Modifier.size(theme.roundButtonSize.dp),
            ) {
                expanded = true
            }
            RoundedCornerGameButton(
                GameButtonType.Start,
                modifier =
                    Modifier.width(theme.functionButtonWidth.dp)
                        .height(theme.functionButtonHeight.dp),
            )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.gamepad_size_large)) },
                onClick = { themeChangedCallback(LARGE).also { expanded = false } },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.gamepad_size_medium)) },
                onClick = { themeChangedCallback(MEDIUM).also { expanded = false } },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.gamepad_size_small)) },
                onClick = { themeChangedCallback(SMALL).also { expanded = false } },
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.gamepad_size_retro)) },
                onClick = { themeChangedCallback(RETRO).also { expanded = false } },
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 70)
@Composable
fun FunctionButtonsPreview() {
    VirtualGamepadTheme { FunctionButtons { _ -> {} } }
}
