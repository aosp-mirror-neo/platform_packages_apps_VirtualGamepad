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

import android.annotation.DrawableRes
import android.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import com.android.virtualgamepad.LocalGamepadState
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.LocalLayoutEditor
import kotlin.math.max
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun GamePadButton(
    @DrawableRes buttonRes: Int,
    @DrawableRes pressedButtonRes: Int,
    @StringRes descriptionRes: Int,
    key: String,
    modifier: Modifier = Modifier,
    onButtonDown: () -> Unit = {},
    onButtonUp: () -> Unit = {},
    onClick: () -> Unit = {},
    // Called when the button is clicked without detecting pressing. This can happen for
    // accessibility or keyboard interactions.
    onNonPressingClick: () -> Unit = {},
) {

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
            onButtonDown()
        }

        DisposableEffect(Unit) {
            onDispose {
                if (!moving && !resizing) {
                    onButtonUp()
                }
            }
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
    val description = stringResource(descriptionRes)
    Box(
        modifier =
            updatedModifier
                .semantics { role = Role.Button }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        onClick()
                        if (!isPressed) {
                            onNonPressingClick()
                        }
                    },
                )
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(if (isPressed) pressedButtonRes else buttonRes),
            contentDescription = description,
        )
    }
}

@Composable
fun GameButton(gameButtonType: GameButtonType, modifier: Modifier = Modifier) {
    val key = gameButtonType.name
    val localView = LocalView.current
    val gamepadState = LocalGamepadState.current
    val theme = LocalGamepadTheme.current
    val scope = rememberCoroutineScope()

    val buttonRes = theme.gameButtonResource(gameButtonType)
    val pressedButtonRes = theme.gameButtonPressedResource(gameButtonType)
    val buttonDescription = theme.gameButtonDescription(gameButtonType)

    GamePadButton(
        buttonRes = buttonRes,
        pressedButtonRes = pressedButtonRes,
        descriptionRes = buttonDescription,
        key = key,
        modifier = modifier,
        onButtonDown = {
            gamepadState?.onButtonDown(gameButtonType)
            if (theme.isHapticsEnabled) {
                Haptics.onPressed(localView)
            }
        },
        onButtonUp = {
            gamepadState?.onButtonUp(gameButtonType)
            if (theme.isHapticsEnabled) {
                Haptics.onReleased(localView)
            }
        },
        onNonPressingClick = {
            scope.launch {
                gamepadState?.onButtonDown(gameButtonType)
                // Add a small delay to simulate a tap. Games might not react correctly if the down
                // event and the up event are too close to each other.
                delay(50)
                gamepadState?.onButtonUp(gameButtonType)
            }
        },
    )
}

@Composable
fun SettingsButton(modifier: Modifier = Modifier, onClick: () -> Unit) {

    val theme = LocalGamepadTheme.current

    val buttonRes = theme.settingsButtonResource()
    val pressedButtonRes = theme.settingsButtonPressedResource()
    val descriptionRes = theme.settingsButtonDescription()

    GamePadButton(
        buttonRes = buttonRes,
        pressedButtonRes = pressedButtonRes,
        descriptionRes = descriptionRes,
        key = "Settings",
        modifier = modifier,
        onButtonDown = {},
        onButtonUp = {},
        onClick = onClick,
    )
}
