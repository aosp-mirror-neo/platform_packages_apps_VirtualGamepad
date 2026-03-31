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

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.scrollBy
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.LocalGamepadState
import com.android.virtualgamepad.R
import com.android.virtualgamepad.ui.GamepadLayoutSpec
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.LocalLayoutEditor
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme
import kotlin.math.max
import kotlin.math.min

private enum class JoystickType {
    JOYSTICK_LEFT,
    JOYSTICK_RIGHT,
}

@Composable
fun LeftJoystick(modifier: Modifier = Modifier) {
    Joystick(modifier = modifier, joystickType = JoystickType.JOYSTICK_LEFT)
}

@Composable
fun RightJoystick(modifier: Modifier = Modifier) {
    Joystick(modifier = modifier, joystickType = JoystickType.JOYSTICK_RIGHT)
}

@Composable
private fun Joystick(modifier: Modifier = Modifier, joystickType: JoystickType) {
    val key = joystickType.name
    val theme = LocalGamepadTheme.current
    val gamepadState = LocalGamepadState.current
    val localView = LocalView.current

    val density = with(LocalDensity.current) { 1.dp.toPx() }
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val layoutSpec = GamepadLayoutSpec(theme)

    val joystickBox = layoutSpec.computeJoystickBoxSize(screenWidthDp)
    val joystickSize = layoutSpec.computeJoystickSize(screenWidthDp)

    val boxSizePx = with(LocalDensity.current) { joystickBox.dp.toPx() }
    val maxDragRangePx = boxSizePx * 0.55f

    // Drag offset in pixels
    val rawDragOffsetPx = remember { mutableStateOf(Offset(0f, 0f)) }
    val boundedDragOffsetPx = remember { mutableStateOf(Offset(0f, 0f)) }
    val boundedDragOffsetDp = remember { mutableStateOf(Offset(0f, 0f)) }

    // Haptic state
    val isDragging = remember { mutableStateOf(false) }
    val isHittingBounds = remember { mutableStateOf(false) }
    val isFirstRun = remember { mutableStateOf(true) }

    LaunchedEffect(isDragging.value) {
        if (isFirstRun.value) {
            // Skip the first run to prevent onReleased() when creating the joystick.
            isFirstRun.value = false
            return@LaunchedEffect
        }
        if (isDragging.value) {
            if (theme.isHapticsEnabled) {
                Haptics.onPressed(localView)
            }
        } else {
            if (theme.isHapticsEnabled) {
                Haptics.onReleased(localView)
            }
        }
    }
    LaunchedEffect(isHittingBounds.value) {
        if (isHittingBounds.value && theme.isHapticsEnabled) {
            Haptics.startBoundaryVibration(localView)
        }
    }

    // Layout editor
    val layoutEditor = LocalLayoutEditor.current
    val moving = layoutEditor.moving
    val resizing = layoutEditor.resizing
    val editorOffsetPx = layoutEditor.offsetPx.getValue(key)
    val editorScale = layoutEditor.scale.getValue(key)

    fun updateJoystickState() {
        // The scale to convert drag events to the (-1.0, 1.0) range for joystick events
        val scale = 2.0f / maxDragRangePx
        when (joystickType) {
            JoystickType.JOYSTICK_LEFT ->
                gamepadState?.onLeftJoystickChanged(
                    (boundedDragOffsetPx.value.x * scale).coerceIn(-1.0f, 1.0f),
                    (boundedDragOffsetPx.value.y * scale).coerceIn(-1.0f, 1.0f),
                )

            JoystickType.JOYSTICK_RIGHT ->
                gamepadState?.onRightJoystickChanged(
                    (boundedDragOffsetPx.value.x * scale).coerceIn(-1.0f, 1.0f),
                    (boundedDragOffsetPx.value.y * scale).coerceIn(-1.0f, 1.0f),
                )
        }
    }

    fun updateBoundaryHaptics() {
        isHittingBounds.value = rawDragOffsetPx.value.getDistance() >= maxDragRangePx / 2.0f
    }

    fun addOffset(dragAmount: Offset) {
        rawDragOffsetPx.value += dragAmount

        // Bound the dragging within the joystick area
        val scale = min(maxDragRangePx / 2.0f / rawDragOffsetPx.value.getDistance(), 1f)
        boundedDragOffsetPx.value = rawDragOffsetPx.value * scale
        boundedDragOffsetDp.value = boundedDragOffsetPx.value / density

        updateJoystickState()
        updateBoundaryHaptics()
    }

    fun resetOffset() {
        rawDragOffsetPx.value = Offset(0f, 0f)
        boundedDragOffsetPx.value = Offset(0f, 0f)
        boundedDragOffsetDp.value = Offset(0f, 0f)

        updateJoystickState()
        updateBoundaryHaptics()
    }

    var containerModifier = modifier.size(joystickBox.dp)

    containerModifier =
        with(LocalDensity.current) {
            containerModifier
                .scale(editorScale)
                .offset(editorOffsetPx.x.toDp(), editorOffsetPx.y.toDp())
        }

    // Layout editor logic
    if (moving or resizing) {
        containerModifier =
            containerModifier.pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    when {
                        moving -> {
                            val offset = layoutEditor.offsetPx.getValue(key) + dragAmount
                            layoutEditor.offsetPx[key] = offset
                        }

                        resizing -> {
                            var scale = layoutEditor.scale.getValue(key) + dragAmount.x / 200
                            scale = max(scale, 0.2f)
                            layoutEditor.scale[key] = scale
                        }
                    }
                }
            }
    }
    // End of layout editor logic

    // Labels for accessibility
    val joystickDescriptionRes =
        if (joystickType == JoystickType.JOYSTICK_LEFT) {
            R.string.description_left_joystick
        } else {
            R.string.description_right_joystick
        }
    val joystickDescription = stringResource(joystickDescriptionRes)

    val actionUp = stringResource(R.string.action_joystick_up_template, joystickDescription)
    val actionDown = stringResource(R.string.action_joystick_down_template, joystickDescription)
    val actionLeft = stringResource(R.string.action_joystick_left_template, joystickDescription)
    val actionRight = stringResource(R.string.action_joystick_right_template, joystickDescription)
    val actionReset = stringResource(R.string.action_joystick_reset_template, joystickDescription)

    val stateCentered = stringResource(R.string.state_joystick_centered)
    val stateMoved = stringResource(R.string.state_joystick_moved)

    // Joystick container box
    Box(
        modifier =
            containerModifier
                .semantics {
                    contentDescription = joystickDescription
                    stateDescription =
                        if (boundedDragOffsetPx.value == Offset(0f, 0f)) stateCentered
                        else stateMoved
                    onClick(label = actionReset) {
                        resetOffset()
                        true
                    }
                    scrollBy { x, y ->
                        addOffset(Offset(x, y))
                        true
                    }
                    customActions =
                        listOf(
                            CustomAccessibilityAction(actionUp) {
                                resetOffset()
                                addOffset(Offset(0f, -maxDragRangePx / 2.0f))
                                true
                            },
                            CustomAccessibilityAction(actionDown) {
                                resetOffset()
                                addOffset(Offset(0f, maxDragRangePx / 2.0f))
                                true
                            },
                            CustomAccessibilityAction(actionLeft) {
                                resetOffset()
                                addOffset(Offset(-maxDragRangePx / 2.0f, 0f))
                                true
                            },
                            CustomAccessibilityAction(actionRight) {
                                resetOffset()
                                addOffset(Offset(maxDragRangePx / 2.0f, 0f))
                                true
                            },
                            CustomAccessibilityAction(actionReset) {
                                resetOffset()
                                true
                            },
                        )
                }
                .focusable()
    ) {
        // Background area
        Box(
            modifier =
                Modifier.size(joystickBox.dp)
                    .clip(CircleShape)
                    .background(Brush.linearGradient(theme.joystickBackgroundColors))
                    .let { baseModifier ->
                        if (moving or resizing) {
                            baseModifier
                        } else {
                            baseModifier.pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        isDragging.value = true

                                        // The offset is relative to the top-left corner of the
                                        // containing box, so we need to convert it to relative to
                                        // the
                                        // center of the box.
                                        addOffset(
                                            offset - Offset(boxSizePx / 2.0f, boxSizePx / 2.0f)
                                        )
                                    },
                                    onDragEnd = {
                                        resetOffset()
                                        isDragging.value = false
                                    },
                                    onDragCancel = {
                                        resetOffset()
                                        isDragging.value = false
                                    },
                                    onDrag = { change, dragAmount ->
                                        change.consume()
                                        addOffset(dragAmount)
                                    },
                                )
                            }
                        }
                    }
        )

        // Stick container to handle drag events. This avoids issues related to rotations of the
        // child views.
        Box(
            modifier =
                Modifier.align(Alignment.Center)
                    .size(joystickSize.dp)
                    .offset(x = boundedDragOffsetDp.value.x.dp, y = boundedDragOffsetDp.value.y.dp)
        ) {
            StickView(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true, widthDp = 161, heightDp = 161)
@Composable
fun JoystickPreview() {
    VirtualGamepadTheme { LeftJoystick() }
}
