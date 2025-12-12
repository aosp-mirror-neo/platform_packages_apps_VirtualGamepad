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

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

@Composable
fun DPad() {
    val theme = LocalGamepadTheme.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        GameButton2(
            GameButtonType.DPAD_UP,
            modifier = Modifier.align(Alignment.TopCenter).size(theme.roundButtonSize.dp),
        )

        GameButton2(
            GameButtonType.DPAD_DOWN,
            modifier = Modifier.align(Alignment.BottomCenter).size(theme.roundButtonSize.dp),
        )

        GameButton2(
            GameButtonType.DPAD_LEFT,
            modifier = Modifier.align(Alignment.CenterStart).size(theme.roundButtonSize.dp),
        )

        GameButton2(
            GameButtonType.DPAD_RIGHT,
            modifier = Modifier.align(Alignment.CenterEnd).size(theme.roundButtonSize.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 189, heightDp = 189)
@Composable
fun GamepadPreview() {
    VirtualGamepadTheme { DPad() }
}
