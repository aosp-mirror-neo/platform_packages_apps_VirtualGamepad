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

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringArrayResource
import com.android.virtualgamepad.R
import com.android.virtualgamepad.ui.theme.GamepadColorTheme
import com.android.virtualgamepad.ui.theme.GamepadSize
import com.android.virtualgamepad.ui.theme.GamepadStyle

object StringResourceAdapter {
    private const val COLOR_THEME_INDEX_LIGHT = 0
    private const val COLOR_THEME_INDEX_DARK = 1

    private const val SIZE_INDEX_SMALL = 0
    private const val SIZE_INDEX_MEDIUM = 1
    private const val SIZE_INDEX_LARGE = 2

    private const val GAMEPAD_STYLE_INDEX_INLINE = 0
    private const val GAMEPAD_STYLE_INDEX_INVERTED = 1

    @Composable
    fun gamepadSizeString(size: GamepadSize): String {
        val index =
            when (size) {
                GamepadSize.LARGE -> SIZE_INDEX_LARGE
                GamepadSize.MEDIUM -> SIZE_INDEX_MEDIUM
                GamepadSize.SMALL -> SIZE_INDEX_SMALL
            }
        return stringArrayResource(R.array.gamepad_size_items)[index]
    }

    fun colorThemeFromIndex(index: Int): GamepadColorTheme {
        return when (index) {
            COLOR_THEME_INDEX_LIGHT -> GamepadColorTheme.LIGHT
            COLOR_THEME_INDEX_DARK -> GamepadColorTheme.DARK
            else -> GamepadColorTheme.LIGHT
        }
    }

    fun gamepadSizeFromIndex(index: Int): GamepadSize {
        return when (index) {
            SIZE_INDEX_SMALL -> GamepadSize.SMALL
            SIZE_INDEX_MEDIUM -> GamepadSize.MEDIUM
            SIZE_INDEX_LARGE -> GamepadSize.LARGE
            else -> GamepadSize.MEDIUM
        }
    }

    fun gamepadStyleFromIndex(index: Int): GamepadStyle {
        return when (index) {
            GAMEPAD_STYLE_INDEX_INLINE -> GamepadStyle.INLINE
            GAMEPAD_STYLE_INDEX_INVERTED -> GamepadStyle.INVERTED
            else -> GamepadStyle.INLINE
        }
    }
}
