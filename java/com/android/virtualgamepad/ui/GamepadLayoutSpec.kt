/*
 * Copyright 2026 The Android Open Source Project
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

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.android.virtualgamepad.ui.theme.GamepadSize
import com.android.virtualgamepad.ui.theme.GamepadTheme

class GamepadLayoutSpec(private val gamepadTheme: GamepadTheme) {

    private fun getRoundButtonSize() =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 70
            GamepadSize.MEDIUM -> 60
            GamepadSize.SMALL -> 56
        }

    private fun getLeftMargin() =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 20
            GamepadSize.MEDIUM -> 30
            GamepadSize.SMALL -> 30
        }

    private fun getRightMargin() =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 20
            GamepadSize.MEDIUM -> 30
            GamepadSize.SMALL -> 30
        }

    private fun getBottomMargin() =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 110
            GamepadSize.MEDIUM -> 135
            GamepadSize.SMALL -> 135
        }

    @Composable
    fun computeLeftPanelConstraintSet(): ConstraintSet {
        return ConstraintSet {
            val dpadUpRef = createRefFor(dpadUpId)
            val dpadLeftRef = createRefFor(dpadLeftId)
            val dpadRightRef = createRefFor(dpadRightId)
            val dpadDownRef = createRefFor(dpadDownId)
            val buttonSize = getRoundButtonSize()

            val l3 = createRefFor(l3Id)
            val joystick = createRefFor(leftJoystickId)
            val shoulderButtons = createRefFor(leftShoulderButtonsId)

            constrain(dpadLeftRef) {
                absoluteLeft.linkTo(parent.absoluteLeft, margin = getLeftMargin().dp)
                bottom.linkTo(dpadDownRef.top)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(dpadDownRef) {
                bottom.linkTo(parent.bottom, margin = getBottomMargin().dp)
                absoluteLeft.linkTo(dpadLeftRef.absoluteRight)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(dpadRightRef) {
                top.linkTo(dpadUpRef.bottom)
                absoluteLeft.linkTo(dpadDownRef.absoluteRight)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(dpadUpRef) {
                bottom.linkTo(dpadLeftRef.top)
                absoluteRight.linkTo(dpadRightRef.absoluteLeft)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(l3) {
                absoluteRight.linkTo(dpadRightRef.absoluteRight)
                top.linkTo(dpadDownRef.bottom)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(joystick) {
                absoluteLeft.linkTo(dpadRightRef.absoluteRight, margin = 8.dp)
                top.linkTo(shoulderButtons.bottom, margin = 8.dp)
            }
            constrain(shoulderButtons) {
                top.linkTo(dpadUpRef.top)
                bottom.linkTo(dpadUpRef.top)
                absoluteLeft.linkTo(l3.absoluteLeft)
            }
        }
    }

    @Composable
    fun computeRightPanelConstraintSet(): ConstraintSet {
        return ConstraintSet {
            val faceButtonA = createRefFor(faceButtonAId)
            val faceButtonB = createRefFor(faceButtonBId)
            val faceButtonX = createRefFor(faceButtonXId)
            val faceButtonY = createRefFor(faceButtonYId)
            val buttonSize = getRoundButtonSize()

            val r3 = createRefFor(r3Id)
            val joystick = createRefFor(rightJoystickId)
            val shoulderButtons = createRefFor(rightShoulderButtonsId)

            constrain(faceButtonB) {
                absoluteRight.linkTo(parent.absoluteRight, margin = getRightMargin().dp)
                bottom.linkTo(faceButtonA.top)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(faceButtonA) {
                bottom.linkTo(parent.bottom, margin = getBottomMargin().dp)
                absoluteRight.linkTo(faceButtonB.absoluteLeft)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(faceButtonX) {
                absoluteRight.linkTo(faceButtonA.absoluteLeft)
                bottom.linkTo(faceButtonA.top)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(faceButtonY) {
                absoluteRight.linkTo(faceButtonB.absoluteLeft)
                bottom.linkTo(faceButtonB.top)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(r3) {
                absoluteLeft.linkTo(faceButtonX.absoluteLeft)
                top.linkTo(faceButtonA.bottom)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(joystick) {
                absoluteRight.linkTo(faceButtonX.absoluteLeft, margin = 8.dp)
                top.linkTo(shoulderButtons.bottom, margin = 8.dp)
            }
            constrain(shoulderButtons) {
                top.linkTo(faceButtonY.top)
                bottom.linkTo(faceButtonY.top)
                absoluteRight.linkTo(r3.absoluteRight)
            }
        }
    }

    companion object {
        const val dpadUpId = "dpadUp"
        const val dpadLeftId = "dpadLeft"
        const val dpadRightId = "dpadRight"
        const val dpadDownId = "dpadDown"
        const val faceButtonsId = "faceButtons"
        const val faceButtonAId = "faceButtonA"
        const val faceButtonBId = "faceButtonB"
        const val faceButtonXId = "faceButtonX"
        const val faceButtonYId = "faceButtonY"
        const val l3Id = "l3"
        const val r3Id = "r3"
        const val rightJoystickId = "rightJoystick"
        const val leftJoystickId = "leftJoystick"
        const val leftShoulderButtonsId = "leftShoulderButtons"
        const val rightShoulderButtonsId = "rightShoulderButtons"
    }
}
