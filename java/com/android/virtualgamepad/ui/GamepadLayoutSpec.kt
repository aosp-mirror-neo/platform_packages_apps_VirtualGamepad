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

class GamepadLayoutSpec {

    @Composable
    fun computeLeftPanelConstraintSet(): ConstraintSet {
        return ConstraintSet {
            val dpad = createRefFor(dpadId)
            val l3 = createRefFor(l3Id)
            val joystick = createRefFor(leftJoystickId)
            val shoulderButtons = createRefFor(leftShoulderButtonsId)

            constrain(dpad) {
                absoluteLeft.linkTo(parent.absoluteLeft, margin = 20.dp)
                bottom.linkTo(parent.bottom, margin = 110.dp)
            }
            constrain(l3) {
                absoluteRight.linkTo(dpad.absoluteRight)
                top.linkTo(dpad.bottom)
            }
            constrain(joystick) {
                absoluteLeft.linkTo(dpad.absoluteRight, margin = 8.dp)
                top.linkTo(shoulderButtons.bottom, margin = 8.dp)
            }
            constrain(shoulderButtons) {
                top.linkTo(dpad.top)
                bottom.linkTo(dpad.top)
                absoluteLeft.linkTo(l3.absoluteLeft)
            }
        }
    }

    @Composable
    fun computeRightPanelConstraintSet(): ConstraintSet {
        return ConstraintSet {
            val faceButtons = createRefFor(faceButtonsId)
            val r3 = createRefFor(r3Id)
            val joystick = createRefFor(rightJoystickId)
            val shoulderButtons = createRefFor(rightShoulderButtonsId)

            constrain(faceButtons) {
                absoluteRight.linkTo(parent.absoluteRight, margin = 20.dp)
                bottom.linkTo(parent.bottom, margin = 110.dp)
            }
            constrain(r3) {
                absoluteLeft.linkTo(faceButtons.absoluteLeft)
                top.linkTo(faceButtons.bottom)
            }
            constrain(joystick) {
                absoluteRight.linkTo(faceButtons.absoluteLeft, margin = 8.dp)
                top.linkTo(shoulderButtons.bottom, margin = 8.dp)
            }
            constrain(shoulderButtons) {
                top.linkTo(faceButtons.top)
                bottom.linkTo(faceButtons.top)
                absoluteRight.linkTo(r3.absoluteRight)
            }
        }
    }

    companion object {
        const val dpadId = "dpad"
        const val faceButtonsId = "faceButtons"
        const val l3Id = "l3"
        const val r3Id = "r3"
        const val rightJoystickId = "rightJoystick"
        const val leftJoystickId = "leftJoystick"
        const val leftShoulderButtonsId = "leftShoulderButtons"
        const val rightShoulderButtonsId = "rightShoulderButtons"
    }
}
