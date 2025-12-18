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

import android.view.KeyEvent

enum class GameButtonType(val text: String, val keyCode: Int) {
    DPAD_UP("▲", KeyEvent.KEYCODE_DPAD_UP),
    DPAD_DOWN("▼", KeyEvent.KEYCODE_DPAD_DOWN),
    DPAD_LEFT("◀", KeyEvent.KEYCODE_DPAD_LEFT),
    DPAD_RIGHT("▶", KeyEvent.KEYCODE_DPAD_RIGHT),
    L1("L1", KeyEvent.KEYCODE_BUTTON_L1),
    R1("R1", KeyEvent.KEYCODE_BUTTON_R1),
    L2("L2", KeyEvent.KEYCODE_BUTTON_L2),
    R2("R2", KeyEvent.KEYCODE_BUTTON_R2),
    L3("L3", KeyEvent.KEYCODE_BUTTON_THUMBL),
    R3("R3", KeyEvent.KEYCODE_BUTTON_THUMBR),
    A("A", KeyEvent.KEYCODE_BUTTON_A),
    B("B", KeyEvent.KEYCODE_BUTTON_B),
    X("X", KeyEvent.KEYCODE_BUTTON_X),
    Y("Y", KeyEvent.KEYCODE_BUTTON_Y),
    Settings("Settings", KeyEvent.KEYCODE_F1),
    Menu("Menu", KeyEvent.KEYCODE_BUTTON_START),
}
