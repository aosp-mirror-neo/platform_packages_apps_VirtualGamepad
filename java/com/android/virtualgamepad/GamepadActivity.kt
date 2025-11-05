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

package com.android.virtualgamepad

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import com.android.virtualgamepad.backend.LocalGamepadBackend
import com.android.virtualgamepad.model.GamepadState
import com.android.virtualgamepad.ui.Gamepad
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

val LocalGamepadState = compositionLocalOf<GamepadState?> { null }

class GamepadActivity : ComponentActivity() {
    val gamepadBackend = LocalGamepadBackend()
    val gamepadState = GamepadState(gamepadBackend)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VirtualGamepadTheme {
                CompositionLocalProvider(LocalGamepadState provides gamepadState) { Gamepad() }
            }
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    override fun onStart() {
        super.onStart()
        gamepadBackend.connect(this)
    }

    override fun onStop() {
        super.onStop()
        gamepadBackend.disconnect()
    }
}
