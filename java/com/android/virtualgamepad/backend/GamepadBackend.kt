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

package com.android.virtualgamepad.backend

import android.app.Activity
import com.android.virtualgamepad.model.GameButtonType

interface GamepadBackend {
    fun connect(context: Activity)

    fun disconnect()

    fun onButtonDown(gameButtonType: GameButtonType)

    fun onButtonUp(gameButtonType: GameButtonType)

    fun onLeftJoystick(x: Float, y: Float)

    fun onRightJoystick(z: Float, rz: Float)
}
