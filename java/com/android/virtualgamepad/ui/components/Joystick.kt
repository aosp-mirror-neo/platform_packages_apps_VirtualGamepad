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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.LocalGamepadState
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
    Joystick(JoystickType.JOYSTICK_LEFT, modifier)
}

@Composable
fun RightJoystick(modifier: Modifier = Modifier) {
    Joystick(JoystickType.JOYSTICK_RIGHT, modifier)
}

@Composable
private fun Joystick(joystickType: JoystickType, modifier: Modifier = Modifier) {
    val key = joystickType.name
    val theme = LocalGamepadTheme.current
    val gamepadState = LocalGamepadState.current
    val localView = LocalView.current

    val density = with(LocalDensity.current) { 1.dp.toPx() }
    val boxSizePx = with(LocalDensity.current) { theme.joystickBox.dp.toPx() }
    val maxDragRangePx = boxSizePx * 0.7f // TODO confirm this value
    val outerRingBrush = Brush.horizontalGradient(theme.joystickOuterRingGradient)
    val innerRingBrush = Brush.horizontalGradient(theme.joystickInnerRingGradient)

    // Drag offset in pixels
    val rawDragOffsetPx = remember { mutableStateOf(Offset(0f, 0f)) }
    val boundedDragOffsetPx = remember { mutableStateOf(Offset(0f, 0f)) }
    val boundedDragOffsetDp = remember { mutableStateOf(Offset(0f, 0f)) }

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

    fun addOffset(dragAmount: Offset) {
        rawDragOffsetPx.value += dragAmount

        // Bound the dragging within the joystick area
        val scale = min(maxDragRangePx / 2.0f / rawDragOffsetPx.value.getDistance(), 1f)
        boundedDragOffsetPx.value = rawDragOffsetPx.value * scale
        boundedDragOffsetDp.value = boundedDragOffsetPx.value / density

        updateJoystickState()
    }

    fun resetOffset() {
        rawDragOffsetPx.value = Offset(0f, 0f)
        boundedDragOffsetPx.value = Offset(0f, 0f)
        boundedDragOffsetDp.value = Offset(0f, 0f)
        updateJoystickState()
    }

    var containerModifier = Modifier.size(theme.joystickBox.dp)

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

    // Joystick container box
    Box(modifier = containerModifier) {
        // Background area
        Box(
            modifier =
                Modifier.size(theme.joystickBox.dp)
                    .clip(CircleShape)
                    .background(theme.joystickBackgroundColor)
                    .let {
                        if (moving or resizing) {
                            it
                        } else {
                            it.pointerInput(Unit) {
                                detectDragGestures(
                                    onDragStart = { offset ->
                                        Haptics.onPressed(localView)

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
                                        Haptics.onReleased(localView)
                                    },
                                    onDragCancel = {
                                        resetOffset()
                                        Haptics.onReleased(localView)
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
                    .offset(x = boundedDragOffsetDp.value.x.dp, y = boundedDragOffsetDp.value.y.dp)
                    .clip(CircleShape)
        ) {
            // Joystick outer ring
            Box(
                modifier =
                    Modifier.align(Alignment.Center)
                        .size(theme.joystickOuterRingSize.dp)
                        .rotate(45f)
                        .clip(CircleShape)
                        .background(outerRingBrush)
            ) {
                // Joystick inner circle
                Box(
                    modifier =
                        Modifier.align(Alignment.Center)
                            .size(theme.joystickInnerRingSize.dp)
                            .clip(CircleShape)
                            .rotate(135f)
                            .background(innerRingBrush)
                ) {}
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 161, heightDp = 161)
@Composable
fun JoystickPreview() {
    VirtualGamepadTheme { LeftJoystick() }
}
