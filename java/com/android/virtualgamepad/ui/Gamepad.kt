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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.R
import com.android.virtualgamepad.ui.components.DPad
import com.android.virtualgamepad.ui.components.FaceButtons
import com.android.virtualgamepad.ui.components.FunctionButtons
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
fun Gamepad(initialGamepadTheme: GamepadTheme? = null) {

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    var gamepadTheme by remember {
        mutableStateOf(initialGamepadTheme ?: GamepadTheme(screenWithDp = screenWidthDp))
    }

    val context = LocalContext.current
    val sharedPref = (context as? Activity)?.getPreferences(Context.MODE_PRIVATE)
    val layoutEditor = remember { LayoutEditor().also { it.load(sharedPref) } }

    val toastMessageSaved = stringResource(R.string.toast_saved)

    CompositionLocalProvider(
        LocalGamepadTheme provides gamepadTheme,
        LocalLayoutEditor provides layoutEditor,
    ) {
        Box(modifier = Modifier.fillMaxSize().background(gamepadTheme.backgroundColor)) {

            // Function buttons: select, start, and menu
            Box(
                modifier =
                    Modifier.align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(gamepadTheme.roundButtonSize.dp)
                        .padding(
                            start = gamepadTheme.offsetFunctionButtons.x.dp,
                            end = gamepadTheme.offsetFunctionButtons.x.dp,
                        )
                        .offset(y = gamepadTheme.offsetFunctionButtons.y.dp)
            ) {
                FunctionButtons(
                    onGamepadStyleChanged = { style ->
                        gamepadTheme = gamepadTheme.copy(style = style)
                    },
                    onGamepadSizeChanged = { size ->
                        gamepadTheme = gamepadTheme.copy(size = size)
                    },
                    onGamepadColorThemeChanged = { colorTheme ->
                        gamepadTheme = gamepadTheme.copy(colorTheme = colorTheme)
                    },
                )
            }

            // Left controls: dpad, L1, L2, L3, and left thumb stick
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart) {
                // Dpad
                Box(
                    modifier =
                        Modifier.width(gamepadTheme.fourButtonBoxSize.x.dp)
                            .height(gamepadTheme.fourButtonBoxSize.y.dp)
                            .offset(
                                x = gamepadTheme.offsetFourButtonSet.x.dp,
                                y = -gamepadTheme.offsetFourButtonSet.y.dp,
                            )
                ) {
                    DPad()
                }

                // L1 and L2
                Box(
                    modifier =
                        Modifier.width(gamepadTheme.shoulderButtonBox.dp)
                            .height(gamepadTheme.shoulderButtonBox.dp)
                            .offset(
                                x = gamepadTheme.offsetShoulderButtonSet.x.dp,
                                y = -gamepadTheme.offsetShoulderButtonSet.y.dp,
                            )
                ) {
                    ShoulderButtonsL1L2()
                }

                // L3
                Box(
                    modifier =
                        Modifier.width(gamepadTheme.roundButtonSize.dp)
                            .height(gamepadTheme.roundButtonSize.dp)
                            .offset(
                                x = gamepadTheme.offsetShoulder3.x.dp,
                                y = -gamepadTheme.offsetShoulder3.y.dp,
                            )
                ) {
                    ShoulderButtonsL3()
                }

                // Left joystick
                Box(
                    modifier =
                        Modifier.width(gamepadTheme.joystickBox.dp)
                            .height(gamepadTheme.joystickBox.dp)
                            .offset(
                                x = gamepadTheme.offsetJoystick.x.dp,
                                y = -gamepadTheme.offsetJoystick.y.dp,
                            )
                ) {
                    LeftJoystick()
                }
            }

            // Right controls: face buttons, R1, R2, R3, and right thumb stick
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
                // Face buttons
                Box(
                    modifier =
                        Modifier.width(gamepadTheme.fourButtonBoxSize.x.dp)
                            .height(gamepadTheme.fourButtonBoxSize.y.dp)
                            .offset(
                                x = -gamepadTheme.offsetFourButtonSet.x.dp,
                                y = -gamepadTheme.offsetFourButtonSet.y.dp,
                            )
                ) {
                    FaceButtons()
                }

                // R1 and R2
                Box(
                    modifier =
                        Modifier.width(gamepadTheme.shoulderButtonBox.dp)
                            .height(gamepadTheme.shoulderButtonBox.dp)
                            .offset(
                                x = -gamepadTheme.offsetShoulderButtonSet.x.dp,
                                y = -gamepadTheme.offsetShoulderButtonSet.y.dp,
                            )
                ) {
                    ShoulderButtonsR1R2()
                }

                // R3
                Box(
                    modifier =
                        Modifier.width(gamepadTheme.roundButtonSize.dp)
                            .height(gamepadTheme.roundButtonSize.dp)
                            .offset(
                                x = -gamepadTheme.offsetShoulder3.x.dp,
                                y = -gamepadTheme.offsetShoulder3.y.dp,
                            )
                ) {
                    ShoulderButtonsR3()
                }

                // Right joystick
                Box(
                    modifier =
                        Modifier.width(gamepadTheme.joystickBox.dp)
                            .height(gamepadTheme.joystickBox.dp)
                            .offset(
                                x = -gamepadTheme.offsetJoystick.x.dp,
                                y = -gamepadTheme.offsetJoystick.y.dp,
                            )
                ) {
                    RightJoystick()
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewLargeLight() {
    VirtualGamepadTheme { Gamepad(GamepadTheme.LARGE_LIGHT) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewLargeDark() {
    VirtualGamepadTheme { Gamepad(GamepadTheme.LARGE_DARK) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewMediumLight() {
    VirtualGamepadTheme { Gamepad(GamepadTheme.MEDIUM_LIGHT) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewMediumDark() {
    VirtualGamepadTheme { Gamepad(GamepadTheme.MEDIUM_DARK) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewSmallLight() {
    VirtualGamepadTheme { Gamepad(GamepadTheme.SMALL_LIGHT) }
}

@Preview(showBackground = true, widthDp = 884, heightDp = 426)
@Composable
fun GamepadPreviewSmallDark() {
    VirtualGamepadTheme { Gamepad(GamepadTheme.SMALL_DARK) }
}

@Preview(showBackground = true, widthDp = 600, heightDp = 300)
@Composable
fun GamepadPreviewLargeLightOnSmallerDisplay() {
    VirtualGamepadTheme { Gamepad(GamepadTheme(screenWithDp = 600)) }
}
