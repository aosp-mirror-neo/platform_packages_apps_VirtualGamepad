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

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.LocalGamepadState
import com.android.virtualgamepad.R
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.LocalLayoutEditor
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme
import kotlin.math.max

private enum class GameButtonFlavor {
    CIRCLE,
    ROUNDED,
    DIAMOND,
}

@Composable
private fun GameButton(
    gameButtonType: GameButtonType,
    modifier: Modifier = Modifier,
    gameButtonFlavor: GameButtonFlavor,
    onClick: () -> Unit,
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

    val shape =
        when (gameButtonFlavor) {
            GameButtonFlavor.CIRCLE -> CircleShape
            GameButtonFlavor.DIAMOND -> RoundedCornerShape(theme.diamondButtonCornerRadius)
            GameButtonFlavor.ROUNDED -> RoundedCornerShape(theme.roundedButtonCornerRadius)
        }

    val fontSize =
        when (gameButtonFlavor) {
            GameButtonFlavor.CIRCLE -> theme.fontSizeGameButtons
            GameButtonFlavor.DIAMOND -> theme.fontSizeGameButtons
            GameButtonFlavor.ROUNDED -> theme.fontSizeFunctionButtons
        }

    val rotate =
        when (gameButtonFlavor) {
            GameButtonFlavor.DIAMOND -> 45f
            else -> 0f
        }

    val color =
        when (gameButtonType) {
            GameButtonType.A -> theme.buttonAColor
            GameButtonType.B -> theme.buttonBColor
            GameButtonType.X -> theme.buttonXColor
            GameButtonType.Y -> theme.buttonYColor
            else -> theme.buttonTextColor
        }

    // Layout editor logic
    var updatedModifier = modifier.scale(scale = editorScale)

    // This is required even without editing
    updatedModifier = updatedModifier.rotate(rotate)

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
        colors = ButtonDefaults.buttonColors(containerColor = theme.buttonColor),
        shape = shape,
        onClick = onClick,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp),
    ) {
        when (gameButtonType) {
            GameButtonType.Mode ->
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = color,
                    contentDescription = stringResource(R.string.description_mode_button),
                )
            else ->
                Text(
                    gameButtonType.text,
                    modifier = Modifier.rotate(-rotate),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Black,
                    fontSize = fontSize,
                    color = color,
                )
        }
    }
}

@Composable
fun CircleGameButton(
    gameButtonType: GameButtonType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    GameButton(gameButtonType, modifier, GameButtonFlavor.CIRCLE, onClick)
}

@Composable
fun RoundedCornerGameButton(
    gameButtonType: GameButtonType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    GameButton(gameButtonType, modifier, GameButtonFlavor.ROUNDED, onClick)
}

@Composable
fun DiamondGameButton(
    gameButtonType: GameButtonType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    GameButton(gameButtonType, modifier, GameButtonFlavor.DIAMOND, onClick)
}

@Preview(showBackground = true, widthDp = 70, heightDp = 70)
@Composable
fun CircleButtonPreview() {
    VirtualGamepadTheme { CircleGameButton(GameButtonType.X) }
}

@Preview(showBackground = true, widthDp = 68, heightDp = 34)
@Composable
fun RoundedGameButtonPreview() {
    VirtualGamepadTheme { RoundedCornerGameButton(GameButtonType.Select) }
}

@Preview(showBackground = true, widthDp = 70, heightDp = 70)
@Composable
fun DiamondGameButtonPreview() {
    VirtualGamepadTheme { DiamondGameButton(GameButtonType.L1) }
}

@Preview(showBackground = true, widthDp = 70, heightDp = 70)
@Composable
fun ModeButtonPreview() {
    VirtualGamepadTheme { CircleGameButton(GameButtonType.Mode) }
}
