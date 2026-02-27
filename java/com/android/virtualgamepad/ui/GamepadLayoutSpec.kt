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
import com.android.virtualgamepad.ui.theme.GamepadStyle
import com.android.virtualgamepad.ui.theme.GamepadTheme

/**
 * Specifies the layout properties for the controls on the gamepad. This controls the margins and
 * placement of all the buttons and controls. The controls are assumed to be pinned close to the
 * bottom left and right parts of the view.
 */
class GamepadLayoutSpec(private val gamepadTheme: GamepadTheme) {

    /**
     * Returns a unit to establish a coordinate system for the UI components without having large
     * scales. This is the size of the small round button. This helps place components when the size
     * of the screen is too small. As an example, instead of placing the joystick 100dp from the
     * left edge on a small screen it would be around 2 units away.
     */
    private fun getUnitSize(): Int = 56

    fun getFunctionButtonWidth() = 72

    fun getFunctionButtonHeight() = 48

    fun panelRounding() = .5f

    fun panelX() =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 1.25f
            GamepadSize.MEDIUM -> 1.18f
            GamepadSize.SMALL -> 1.12f
        }

    fun panelY() = .25f

    fun getRoundButtonSize(screenWidthDp: Int) =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 70
            GamepadSize.MEDIUM -> 60
            GamepadSize.SMALL -> 56
        }.adjust(screenWidthDp)

    fun getTopFunctionMargin(screenWidthDp: Int) = 32.adjust(screenWidthDp)

    private fun getShoulderButtonSize(screenWidthDp: Int) =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 80
            GamepadSize.MEDIUM -> 76
            GamepadSize.SMALL -> 66
        }.adjust(screenWidthDp)

    private fun getLeftMargin(screenWidthDp: Int) =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 20
            GamepadSize.MEDIUM -> 30
            GamepadSize.SMALL -> 30
        }.adjust(screenWidthDp)

    private fun getLeftMarginInverted(screenWidthDp: Int) = 16.adjust(screenWidthDp)

    private fun getBottomMarginInverted(screenWidthDp: Int) =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 160
            GamepadSize.MEDIUM -> 150
            GamepadSize.SMALL -> 150
        }.adjust(screenWidthDp)

    private fun getRightMargin(screenWidthDp: Int) =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 20
            GamepadSize.MEDIUM -> 30
            GamepadSize.SMALL -> 30
        }.adjust(screenWidthDp)

    private fun getBottomMargin(screenWidthDp: Int) =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 110
            GamepadSize.MEDIUM -> 135
            GamepadSize.SMALL -> 135
        }.adjust(screenWidthDp)

    private fun getShoulderMargin(screenWidthDp: Int) =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 70
            GamepadSize.MEDIUM -> 50
            GamepadSize.SMALL -> 48
        }.adjust(screenWidthDp)

    private fun getShoulderMarginInverted(screenWidthDp: Int) =
        when (gamepadTheme.size) {
            GamepadSize.LARGE -> 70
            GamepadSize.MEDIUM -> 50
            GamepadSize.SMALL -> 48
        }.adjust(screenWidthDp)

    fun computeJoystickBoxSize(screenWidthDp: Int): Int {
        return 156.adjust(screenWidthDp)
    }

    fun computeJoystickSize(screenWidthDp: Int): Int {
        return 128.adjust(screenWidthDp)
    }

    fun componentMargin(screenWidthDp: Int): Int {
        return 8.adjust(screenWidthDp)
    }

    @Composable
    fun computeLeftPanelConstraintSet(screenWidthDp: Int): ConstraintSet {
        return when (gamepadTheme.style) {
            GamepadStyle.INLINE -> {
                computeInlineLeftPanelConstraintSet(screenWidthDp = screenWidthDp)
            }
            GamepadStyle.INVERTED -> {
                computeInvertedLeftPanelConstraintSet(screenWidthDp = screenWidthDp)
            }
        }
    }

    private fun computeInvertedLeftPanelConstraintSet(screenWidthDp: Int): ConstraintSet {
        return ConstraintSet {
            val dpadUpRef = createRefFor(dpadUpId)
            val dpadLeftRef = createRefFor(dpadLeftId)
            val dpadRightRef = createRefFor(dpadRightId)
            val dpadDownRef = createRefFor(dpadDownId)
            val buttonSize = getRoundButtonSize(screenWidthDp = screenWidthDp)

            val l1 = createRefFor(l1Id)
            val l2 = createRefFor(l2Id)
            val l3 = createRefFor(l3Id)
            val shoulderSize = getShoulderButtonSize(screenWidthDp)

            val joystick = createRefFor(leftJoystickId)

            val leftPanelBackground = createRefFor(leftPanelBackgroundId)

            constrain(joystick) {
                absoluteLeft.linkTo(
                    parent.absoluteLeft,
                    margin = getLeftMarginInverted(screenWidthDp).dp,
                )
                bottom.linkTo(parent.bottom, margin = getBottomMarginInverted(screenWidthDp).dp)
            }

            constrain(dpadLeftRef) {
                top.linkTo(joystick.bottom, margin = componentMargin(screenWidthDp).dp)
                absoluteRight.linkTo(joystick.absoluteRight)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(dpadDownRef) {
                absoluteLeft.linkTo(dpadLeftRef.absoluteRight)
                top.linkTo(dpadLeftRef.bottom)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(dpadRightRef) {
                absoluteLeft.linkTo(dpadDownRef.absoluteRight)
                bottom.linkTo(dpadDownRef.top)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(dpadUpRef) {
                bottom.linkTo(dpadLeftRef.top)
                absoluteLeft.linkTo(dpadLeftRef.absoluteRight)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(l1) {
                absoluteLeft.linkTo(dpadUpRef.absoluteLeft)
                absoluteRight.linkTo(dpadUpRef.absoluteRight)
                bottom.linkTo(dpadUpRef.top, margin = getShoulderMarginInverted(screenWidthDp).dp)
                width = Dimension.value(shoulderSize.dp)
                height = Dimension.value(shoulderSize.dp)
            }
            constrain(l2) {
                absoluteLeft.linkTo(dpadRightRef.absoluteLeft)
                absoluteRight.linkTo(dpadRightRef.absoluteRight)
                bottom.linkTo(
                    dpadRightRef.top,
                    margin = getShoulderMarginInverted(screenWidthDp).dp,
                )
                width = Dimension.value(shoulderSize.dp)
                height = Dimension.value(shoulderSize.dp)
            }
            constrain(l3) {
                bottom.linkTo(dpadRightRef.top)
                absoluteLeft.linkTo(dpadRightRef.absoluteRight)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(leftPanelBackground) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        }
    }

    private fun computeInlineLeftPanelConstraintSet(screenWidthDp: Int): ConstraintSet {
        return ConstraintSet {
            val dpadUpRef = createRefFor(dpadUpId)
            val dpadLeftRef = createRefFor(dpadLeftId)
            val dpadRightRef = createRefFor(dpadRightId)
            val dpadDownRef = createRefFor(dpadDownId)
            val buttonSize = getRoundButtonSize(screenWidthDp = screenWidthDp)

            val l1 = createRefFor(l1Id)
            val l2 = createRefFor(l2Id)
            val l3 = createRefFor(l3Id)
            val shoulderSize = getShoulderButtonSize(screenWidthDp)

            val joystick = createRefFor(leftJoystickId)

            val leftPanelBackground = createRefFor(leftPanelBackgroundId)

            constrain(dpadLeftRef) {
                absoluteLeft.linkTo(parent.absoluteLeft, margin = getLeftMargin(screenWidthDp).dp)
                bottom.linkTo(dpadDownRef.top)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(dpadDownRef) {
                bottom.linkTo(parent.bottom, margin = getBottomMargin(screenWidthDp).dp)
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
            constrain(l1) {
                absoluteLeft.linkTo(dpadRightRef.absoluteLeft)
                absoluteRight.linkTo(dpadRightRef.absoluteRight)
                bottom.linkTo(dpadRightRef.top, margin = getShoulderMargin(screenWidthDp).dp)
                width = Dimension.value(shoulderSize.dp)
                height = Dimension.value(shoulderSize.dp)
            }
            constrain(l2) {
                top.linkTo(dpadUpRef.top)
                bottom.linkTo(dpadUpRef.bottom)
                absoluteLeft.linkTo(
                    dpadUpRef.absoluteRight,
                    margin = getShoulderMargin(screenWidthDp).dp,
                )
                width = Dimension.value(shoulderSize.dp)
                height = Dimension.value(shoulderSize.dp)
            }
            constrain(l3) {
                absoluteRight.linkTo(dpadRightRef.absoluteRight)
                top.linkTo(dpadDownRef.bottom)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(joystick) {
                absoluteLeft.linkTo(
                    dpadRightRef.absoluteRight,
                    margin = componentMargin(screenWidthDp).dp,
                )
                top.linkTo(l2.bottom, margin = componentMargin(screenWidthDp).dp)
            }
            constrain(leftPanelBackground) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        }
    }

    @Composable
    fun computeRightPanelConstraintSet(screenWidthDp: Int): ConstraintSet {
        return ConstraintSet {
            val faceButtonA = createRefFor(faceButtonAId)
            val faceButtonB = createRefFor(faceButtonBId)
            val faceButtonX = createRefFor(faceButtonXId)
            val faceButtonY = createRefFor(faceButtonYId)
            val buttonSize = getRoundButtonSize(screenWidthDp)

            val r1 = createRefFor(r1Id)
            val r2 = createRefFor(r2Id)
            val r3 = createRefFor(r3Id)
            val shoulderSize = getShoulderButtonSize(screenWidthDp)

            val joystick = createRefFor(rightJoystickId)

            val rightPanelBackground = createRefFor(rightPanelBackgroundId)

            constrain(faceButtonB) {
                absoluteRight.linkTo(
                    parent.absoluteRight,
                    margin = getRightMargin(screenWidthDp).dp,
                )
                bottom.linkTo(faceButtonA.top)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(faceButtonA) {
                bottom.linkTo(parent.bottom, margin = getBottomMargin(screenWidthDp).dp)
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
            constrain(r1) {
                absoluteRight.linkTo(faceButtonX.absoluteRight)
                absoluteLeft.linkTo(faceButtonX.absoluteLeft)
                bottom.linkTo(faceButtonX.top, margin = getShoulderMargin(screenWidthDp).dp)
                width = Dimension.value(shoulderSize.dp)
                height = Dimension.value(shoulderSize.dp)
            }
            constrain(r2) {
                top.linkTo(faceButtonY.top)
                bottom.linkTo(faceButtonY.bottom)
                absoluteRight.linkTo(
                    faceButtonY.absoluteLeft,
                    margin = getShoulderMargin(screenWidthDp).dp,
                )
                width = Dimension.value(shoulderSize.dp)
                height = Dimension.value(shoulderSize.dp)
            }
            constrain(r3) {
                absoluteLeft.linkTo(faceButtonX.absoluteLeft)
                top.linkTo(faceButtonA.bottom)
                width = Dimension.value(buttonSize.dp)
                height = Dimension.value(buttonSize.dp)
            }
            constrain(joystick) {
                absoluteRight.linkTo(
                    faceButtonX.absoluteLeft,
                    margin = componentMargin(screenWidthDp).dp,
                )
                top.linkTo(r2.bottom, margin = componentMargin(screenWidthDp).dp)
            }
            constrain(rightPanelBackground) {
                absoluteLeft.linkTo(parent.absoluteLeft)
                absoluteRight.linkTo(parent.absoluteRight)
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
        }
    }

    // The design is based the width of 883dp. If the width is smaller, UI needs to be scaled.
    /**
     * Scales the value if the width is smaller than [widthDpBase]. The designs are based on a width
     * of 883dp. If the width is smaller, UI components may not fit properly so we scale them.
     */
    private fun Int.adjust(screenWidthDp: Int): Int {
        return this
        //        if (screenWidthDp >= widthDpBase) return this
        //        return this * screenWidthDp / widthDpBase
    }

    companion object {
        const val dpadUpId = "dpadUp"
        const val dpadLeftId = "dpadLeft"
        const val dpadRightId = "dpadRight"
        const val dpadDownId = "dpadDown"
        const val faceButtonAId = "faceButtonA"
        const val faceButtonBId = "faceButtonB"
        const val faceButtonXId = "faceButtonX"
        const val faceButtonYId = "faceButtonY"
        const val l1Id = "l1"
        const val l2Id = "l2"
        const val l3Id = "l3"
        const val r1Id = "r1"
        const val r2Id = "r2"
        const val r3Id = "r3"
        const val rightJoystickId = "rightJoystick"
        const val leftJoystickId = "leftJoystick"
        const val leftPanelBackgroundId = "leftPanelBackground"
        const val rightPanelBackgroundId = "rightPanelBackground"
    }
}
