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

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.LocalGamepadState
import com.android.virtualgamepad.R
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.LocalLayoutEditor
import com.android.virtualgamepad.ui.theme.GamepadColorTheme
import kotlin.math.max

@Composable
fun GameButton(
    gameButtonType: GameButtonType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {

    val key = gameButtonType.name
    val localView = LocalView.current
    val gamepadState = LocalGamepadState.current
    val theme = LocalGamepadTheme.current

    // Layout editor
    val layoutEditor = LocalLayoutEditor.current
    val moving = layoutEditor.moving
    val resizing = layoutEditor.resizing
    val editorOffsetPx = layoutEditor.offsetPx.getValue(key)
    val editorScale = layoutEditor.scale.getValue(key)

    // Handle pressed state
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    if (isPressed) {
        if (!moving && !resizing) {
            gamepadState?.onButtonDown(gameButtonType)
            Haptics.onPressed(localView)
        }

        DisposableEffect(Unit) {
            onDispose {
                if (!moving && !resizing) {
                    gamepadState?.onButtonUp(gameButtonType)
                    Haptics.onReleased(localView)
                }
            }
        }
    }

    val buttonRes =
        when (gameButtonType) {
            GameButtonType.A ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_c_a
                } else {
                    R.drawable.button_c_a_dark
                }
            GameButtonType.B ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_c_b
                } else {
                    R.drawable.button_c_b_dark
                }
            GameButtonType.X ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_c_x
                } else {
                    R.drawable.button_c_x_dark
                }
            GameButtonType.Y ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_c_y
                } else {
                    R.drawable.button_c_y_dark
                }
            GameButtonType.L1 ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_l1
                } else {
                    R.drawable.button_l1_dark
                }
            GameButtonType.L2 ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_l2
                } else {
                    R.drawable.button_l2_dark
                }
            GameButtonType.L3 ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_l3
                } else {
                    R.drawable.button_l3_dark
                }
            GameButtonType.R1 ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_r1
                } else {
                    R.drawable.button_r1_dark
                }
            GameButtonType.R2 ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_r2
                } else {
                    R.drawable.button_r2_dark
                }
            GameButtonType.R3 ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_r3
                } else {
                    R.drawable.button_r3_dark
                }
            GameButtonType.DPAD_UP ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_d_u
                } else {
                    R.drawable.button_d_u_dark
                }
            GameButtonType.DPAD_DOWN ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_d_d
                } else {
                    R.drawable.button_d_d_dark
                }
            GameButtonType.DPAD_LEFT ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_d_l
                } else {
                    R.drawable.button_d_l_dark
                }
            GameButtonType.DPAD_RIGHT ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_d_r
                } else {
                    R.drawable.button_d_r_dark
                }
            GameButtonType.Menu ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_menu
                } else {
                    R.drawable.button_menu_dark
                }
            GameButtonType.Settings ->
                if (theme.colorTheme == GamepadColorTheme.LIGHT) {
                    R.drawable.button_settings
                } else {
                    R.drawable.button_settings_dark
                }
        }

    // Layout editor logic
    var updatedModifier = modifier.scale(scale = editorScale)

    // Layout editor logic
    updatedModifier =
        with(LocalDensity.current) {
            updatedModifier.offset(editorOffsetPx.x.toDp(), editorOffsetPx.y.toDp())
        }
    if (moving) {
        updatedModifier =
            updatedModifier.pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    val offset = layoutEditor.offsetPx.getValue(key) + dragAmount
                    layoutEditor.offsetPx[key] = offset
                }
            }
    } else if (resizing) {
        updatedModifier =
            updatedModifier.pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    var scale = layoutEditor.scale.getValue(key) + dragAmount.x / 200
                    scale = max(scale, 0.2f)
                    layoutEditor.scale[key] = scale
                }
            }
    }
    // End of layout editor logic

    Button(
        modifier = updatedModifier,
        onClick = onClick,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                contentColor = androidx.compose.ui.graphics.Color.Transparent,
            ),
    ) {
        Image(
            painter = painterResource(buttonRes),
            contentDescription = stringResource(R.string.description_mode_button),
        )
    }
}
