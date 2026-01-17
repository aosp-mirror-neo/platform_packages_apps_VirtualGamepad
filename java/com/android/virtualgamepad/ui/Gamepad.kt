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
import androidx.compose.foundation.layout.size
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
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.components.FunctionButtons
import com.android.virtualgamepad.ui.components.GameButton
import com.android.virtualgamepad.ui.components.GamepadBackground
import com.android.virtualgamepad.ui.components.LeftJoystick
import com.android.virtualgamepad.ui.components.RightJoystick
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

    val joystickBox = gamepadTheme.getJoystickBox(screenWidthDp)
    val layoutSpec = GamepadLayoutSpec(gamepadTheme)

    val constraints = layoutSpec.computeLeftPanelConstraintSet()

    ConstraintLayout(constraintSet = constraints, modifier = modifier.fillMaxSize()) {
        GameButton(GameButtonType.DPAD_UP, modifier = Modifier.layoutId(GamepadLayoutSpec.dpadUpId))

        GameButton(
            GameButtonType.DPAD_DOWN,
            modifier = Modifier.layoutId(GamepadLayoutSpec.dpadDownId),
        )

        GameButton(
            GameButtonType.DPAD_LEFT,
            modifier = Modifier.layoutId(GamepadLayoutSpec.dpadLeftId),
        )

        GameButton(
            GameButtonType.DPAD_RIGHT,
            modifier = Modifier.layoutId(GamepadLayoutSpec.dpadRightId),
        )

        // L1 and L2
        GameButton(GameButtonType.L1, modifier = Modifier.layoutId(GamepadLayoutSpec.l1Id))

        GameButton(GameButtonType.L2, modifier = Modifier.layoutId(GamepadLayoutSpec.l2Id))

        // L3
        GameButton(GameButtonType.L3, modifier = Modifier.layoutId(GamepadLayoutSpec.l3Id))

        // Left joystick
        LeftJoystick(
            modifier =
                Modifier.width(joystickBox.dp)
                    .height(joystickBox.dp)
                    .layoutId(GamepadLayoutSpec.leftJoystickId)
        )
    }
}

/** Right control panel: face buttons, R1, R2, R3, and right thumb stick. */
@Composable
fun RightControlPanel(modifier: Modifier) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val gamepadTheme = LocalGamepadTheme.current

    val shoulderButtonBox = gamepadTheme.getShoulderButtonBox(screenWidthDp)
    val joystickBox = gamepadTheme.getJoystickBox(screenWidthDp)

    val layoutSpec = GamepadLayoutSpec(gamepadTheme)

    val constraintSet = layoutSpec.computeRightPanelConstraintSet()

    ConstraintLayout(constraintSet = constraintSet, modifier = modifier.fillMaxSize()) {
        GameButton(GameButtonType.A, modifier = Modifier.layoutId(GamepadLayoutSpec.faceButtonAId))

        GameButton(GameButtonType.B, modifier = Modifier.layoutId(GamepadLayoutSpec.faceButtonBId))

        GameButton(GameButtonType.X, modifier = Modifier.layoutId(GamepadLayoutSpec.faceButtonXId))

        GameButton(GameButtonType.Y, modifier = Modifier.layoutId(GamepadLayoutSpec.faceButtonYId))

        // R1 and R2
        GameButton(GameButtonType.R1, modifier = Modifier.layoutId(GamepadLayoutSpec.r1Id))

        GameButton(GameButtonType.R2, modifier = Modifier.layoutId(GamepadLayoutSpec.r2Id))

        // R3
        GameButton(GameButtonType.R3, modifier = Modifier.layoutId(GamepadLayoutSpec.r3Id))

        // Right joystick
        RightJoystick(
            modifier =
                Modifier.width(joystickBox.dp)
                    .height(joystickBox.dp)
                    .layoutId(GamepadLayoutSpec.rightJoystickId)
        )
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
