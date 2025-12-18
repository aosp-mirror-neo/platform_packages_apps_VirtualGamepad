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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme
import kotlin.math.sqrt

@Composable
fun ShoulderButtonsL1L2() {
    val theme = LocalGamepadTheme.current

    // This ensure that the diamond button (rotated square button) doesn't go off the bounding box.
    val offset = (sqrt(2f) - 1) * theme.squareButtonSize / 2

    Box(modifier = Modifier.fillMaxSize()) {
        GameButton(
            GameButtonType.L1,
            modifier =
                Modifier.align(Alignment.TopStart)
                    .size(theme.squareButtonSize.dp)
                    .offset(x = offset.dp, y = offset.dp),
        )

        GameButton(
            GameButtonType.L2,
            modifier =
                Modifier.align(Alignment.BottomEnd)
                    .size(theme.squareButtonSize.dp)
                    .offset(x = -offset.dp, y = -offset.dp),
        )
    }
}

@Composable
fun ShoulderButtonsR1R2() {
    val theme = LocalGamepadTheme.current

    // This ensure that the diamond button (rotated square button) doesn't go off the bounding box.
    val offset = (sqrt(2f) - 1) * theme.squareButtonSize / 2

    Box(modifier = Modifier.fillMaxSize()) {
        GameButton(
            GameButtonType.R1,
            modifier =
                Modifier.align(Alignment.TopEnd)
                    .size(theme.squareButtonSize.dp)
                    .offset(x = -offset.dp, y = offset.dp),
        )

        GameButton(
            GameButtonType.R2,
            modifier =
                Modifier.align(Alignment.BottomStart)
                    .size(theme.squareButtonSize.dp)
                    .offset(x = offset.dp, y = -offset.dp),
        )
    }
}

@Composable
fun ShoulderButtonsL3() {
    val theme = LocalGamepadTheme.current
    Box(modifier = Modifier.fillMaxSize()) {
        GameButton(
            GameButtonType.L3,
            modifier = Modifier.align(Alignment.Center).size(theme.roundButtonSize.dp),
        )
    }
}

@Composable
fun ShoulderButtonsR3() {
    val theme = LocalGamepadTheme.current
    Box(modifier = Modifier.fillMaxSize()) {
        GameButton(
            GameButtonType.R3,
            modifier = Modifier.align(Alignment.Center).size(theme.roundButtonSize.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 158, heightDp = 158)
@Composable
fun ShoulderButtonsL1L2Preview() {
    VirtualGamepadTheme { ShoulderButtonsL1L2() }
}

@Preview(showBackground = true, widthDp = 158, heightDp = 158)
@Composable
fun ShoulderButtonsR1R2Preview() {
    VirtualGamepadTheme { ShoulderButtonsR1R2() }
}

@Preview(showBackground = true, widthDp = 70, heightDp = 70)
@Composable
fun ShoulderButtonsL3Preview() {
    VirtualGamepadTheme { ShoulderButtonsL3() }
}

@Preview(showBackground = true, widthDp = 70, heightDp = 70)
@Composable
fun ShoulderButtonsR3Preview() {
    VirtualGamepadTheme { ShoulderButtonsR3() }
}
