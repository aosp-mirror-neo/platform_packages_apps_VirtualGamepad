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

package com.android.virtualgamepad.ui

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.android.virtualgamepad.ui.components.DPad
import com.android.virtualgamepad.ui.components.FaceButtons
import com.android.virtualgamepad.ui.components.FunctionButtons
import com.android.virtualgamepad.ui.components.GamepadBackground
import com.android.virtualgamepad.ui.components.LeftJoystick
import com.android.virtualgamepad.ui.components.RightJoystick
import com.android.virtualgamepad.ui.components.ShoulderButtonsL1L2
import com.android.virtualgamepad.ui.components.ShoulderButtonsL3
import com.android.virtualgamepad.ui.components.ShoulderButtonsR1R2
import com.android.virtualgamepad.ui.components.ShoulderButtonsR3
import com.android.virtualgamepad.ui.theme.GamepadTheme
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

val LocalGamepadTheme = compositionLocalOf { GamepadTheme.MEDIUM_LIGHT }

val LocalLayoutEditor = compositionLocalOf { LayoutEditor() }

@Composable
fun Gamepad(theme: GamepadTheme? = null, onGamepadThemeChanged: (GamepadTheme) -> Unit = { _ -> }) {
    var gamepadTheme by remember { mutableStateOf(theme ?: GamepadTheme()) }

    val context = LocalContext.current
    val sharedPref = (context as? Activity)?.getPreferences(Context.MODE_PRIVATE)
    val layoutEditor = remember { LayoutEditor().also { it.load(sharedPref) } }

    CompositionLocalProvider(
        LocalGamepadTheme provides gamepadTheme,
        LocalLayoutEditor provides layoutEditor,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GamepadBackground()
            FunctionButtonPanel(
                Modifier.align(Alignment.TopCenter),
                { newGamepadTheme ->
                    gamepadTheme = newGamepadTheme
                    onGamepadThemeChanged(newGamepadTheme)
                },
            )
            Row {
                LeftControlPanel(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(gamepadTheme.getLeftRightSpacerWidth().dp))
                RightControlPanel(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun FunctionButtonPanel(
    modifier: Modifier,
    onGamepadThemeChanged: (GamepadTheme) -> Unit = { _ -> },
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val gamepadTheme = LocalGamepadTheme.current

    val roundButtonSize = gamepadTheme.getRoundButtonSize(screenWidthDp)
    val offsetFunctionButtons = gamepadTheme.getOffsetFunctionButtons(screenWidthDp)
    FunctionButtons(
        modifier =
            modifier
                .fillMaxWidth()
                .height(roundButtonSize.dp)
                .padding(start = offsetFunctionButtons.x.dp, end = offsetFunctionButtons.x.dp)
                .offset(y = offsetFunctionButtons.y.dp),
        onGamepadStyleChanged = { style ->
            onGamepadThemeChanged(gamepadTheme.copy(style = style))
        },
        onGamepadSizeChanged = { size -> onGamepadThemeChanged(gamepadTheme.copy(size = size)) },
        onGamepadColorThemeChanged = { colorTheme ->
            onGamepadThemeChanged(gamepadTheme.copy(colorTheme = colorTheme))
        },
    )
}

/** Left control panel: dpad, L1, L2, L3, and left thumb stick */
@Composable
fun LeftControlPanel(modifier: Modifier) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val gamepadTheme = LocalGamepadTheme.current

    val roundButtonSize = gamepadTheme.getRoundButtonSize(screenWidthDp)
    val shoulderButtonBox = gamepadTheme.getShoulderButtonBox(screenWidthDp)
    val joystickBox = gamepadTheme.getJoystickBox(screenWidthDp)
    val layoutSpec = GamepadLayoutSpec()

    val constraintSet = layoutSpec.computeLeftPanelConstraintSet()

    ConstraintLayout(constraintSet = constraintSet, modifier = modifier.fillMaxSize()) {
        DPad(modifier = Modifier.layoutId(GamepadLayoutSpec.dpadId))

        // L1 and L2
        Box(
            modifier =
                Modifier.width(shoulderButtonBox.dp)
                    .height(shoulderButtonBox.dp)
                    .layoutId(GamepadLayoutSpec.leftShoulderButtonsId)
        ) {
            ShoulderButtonsL1L2()
        }

        // L3
        Box(
            modifier =
                Modifier.width(roundButtonSize.dp)
                    .height(roundButtonSize.dp)
                    .layoutId(GamepadLayoutSpec.l3Id)
        ) {
            ShoulderButtonsL3()
        }

        // Left joystick
        Box(
            modifier =
                Modifier.width(joystickBox.dp)
                    .height(joystickBox.dp)
                    .layoutId(GamepadLayoutSpec.leftJoystickId)
        ) {
            LeftJoystick()
        }
    }
}

/** Right control panel: face buttons, R1, R2, R3, and right thumb stick. */
@Composable
fun RightControlPanel(modifier: Modifier) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val gamepadTheme = LocalGamepadTheme.current

    val roundButtonSize = gamepadTheme.getRoundButtonSize(screenWidthDp)
    val shoulderButtonBox = gamepadTheme.getShoulderButtonBox(screenWidthDp)
    val joystickBox = gamepadTheme.getJoystickBox(screenWidthDp)

    val layoutSpec = GamepadLayoutSpec()

    val constraintSet = layoutSpec.computeRightPanelConstraintSet()

    ConstraintLayout(constraintSet = constraintSet, modifier = modifier.fillMaxSize()) {
        FaceButtons(modifier = Modifier.layoutId(GamepadLayoutSpec.faceButtonsId))

        // R1 and R2
        Box(
            modifier =
                Modifier.width(shoulderButtonBox.dp)
                    .height(shoulderButtonBox.dp)
                    .layoutId(GamepadLayoutSpec.rightShoulderButtonsId)
        ) {
            ShoulderButtonsR1R2()
        }

        // R3
        Box(
            modifier =
                Modifier.width(roundButtonSize.dp)
                    .height(roundButtonSize.dp)
                    .layoutId(GamepadLayoutSpec.r3Id)
        ) {
            ShoulderButtonsR3()
        }

        // Right joystick
        Box(
            modifier =
                Modifier.width(joystickBox.dp)
                    .height(joystickBox.dp)
                    .layoutId(GamepadLayoutSpec.rightJoystickId)
        ) {
            RightJoystick()
        }
    }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewLargeLight() {
    VirtualGamepadTheme { Gamepad(theme = GamepadTheme.LARGE_LIGHT) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewLargeDark() {
    VirtualGamepadTheme { Gamepad(theme = GamepadTheme.LARGE_DARK) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewMediumLight() {
    VirtualGamepadTheme { Gamepad(theme = GamepadTheme.MEDIUM_LIGHT) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewMediumDark() {
    VirtualGamepadTheme { Gamepad(theme = GamepadTheme.MEDIUM_DARK) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewSmallLight() {
    VirtualGamepadTheme { Gamepad(theme = GamepadTheme.SMALL_LIGHT) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewSmallDark() {
    VirtualGamepadTheme { Gamepad(theme = GamepadTheme.SMALL_DARK) }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 300)
@Composable
fun GamepadPreviewLargeLightOnSmallerDisplay() {
    VirtualGamepadTheme { Gamepad(theme = GamepadTheme.SMALL_LIGHT) }
}
