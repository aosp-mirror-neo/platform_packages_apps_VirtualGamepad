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

package com.android.virtualgamepad.model

import com.android.virtualgamepad.backend.GamepadBackend

/**
 * A class to track game button and joystick states. It also calls the backend to inject input
 * events into the target game app.
 */
class GamepadState(val backend: GamepadBackend? = null) {
    object JoyStickState {
        // Left joystick values
        var x = 0.0f
        var y = 0.0f

        // Right joystick values
        var z = 0.0f
        var rz = 0.0f
    }

    fun onButtonDown(button: GameButtonType) {
        backend?.onButtonDown(button)
    }

    fun onButtonUp(button: GameButtonType) {
        backend?.onButtonUp(button)
    }

    fun onLeftJoystickChanged(x: Float, y: Float) {
        JoyStickState.x = x
        JoyStickState.y = y
        backend?.onLeftJoystick(x, y)
    }

    fun onRightJoystickChanged(z: Float, rz: Float) {
        JoyStickState.z = z
        JoyStickState.rz = rz
        backend?.onRightJoystick(z, rz)
    }
}
