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

package com.android.virtualgamepad.backend

import android.content.SharedPreferences
import androidx.core.content.edit
import com.android.virtualgamepad.ui.theme.GamepadColorTheme
import com.android.virtualgamepad.ui.theme.GamepadSize
import com.android.virtualgamepad.ui.theme.GamepadStyle
import com.android.virtualgamepad.ui.theme.GamepadTheme

class SettingsRepo(
    private val defaultTheme: GamepadTheme,
    private val sharedPreferences: SharedPreferences,
) {

    fun setGamepadTheme(gamepadTheme: GamepadTheme) {
        setHapticsEnabled(gamepadTheme.isHapticsEnabled)
        setGamepadColorTheme(gamepadTheme.colorTheme)
        setGamepadSize(gamepadTheme.size)
        setGamepadStyle(gamepadTheme.style)
    }

    fun getGamepadTheme(): GamepadTheme {
        return GamepadTheme(
            isHapticsEnabled = isHapticsEnabled(),
            size = getGamepadSize(),
            style = getGamepadStyle(),
            colorTheme = getGamepadColorTheme(),
        )
    }

    private fun isHapticsEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_HAPTICS_ENABLED, true)
    }

    private fun setHapticsEnabled(enabled: Boolean) {
        sharedPreferences.edit { putBoolean(KEY_HAPTICS_ENABLED, enabled) }
    }

    private fun setGamepadColorTheme(gamepadColorTheme: GamepadColorTheme) {
        val theme =
            when (gamepadColorTheme) {
                GamepadColorTheme.LIGHT -> VALUE_COLOR_THEME_LIGHT
                GamepadColorTheme.DARK -> VALUE_COLOR_THEME_DARK
            }
        sharedPreferences.edit { putInt(KEY_COLOR_THEME, theme) }
    }

    private fun getGamepadColorTheme(): GamepadColorTheme {
        if (sharedPreferences.contains(KEY_COLOR_THEME)) {
            val value = sharedPreferences.getInt(KEY_COLOR_THEME, VALUE_COLOR_THEME_LIGHT)
            return when (value) {
                VALUE_COLOR_THEME_LIGHT -> GamepadColorTheme.LIGHT
                VALUE_COLOR_THEME_DARK -> GamepadColorTheme.DARK
                else -> defaultTheme.colorTheme
            }
        } else {
            val theme =
                when (defaultTheme.colorTheme) {
                    GamepadColorTheme.LIGHT -> VALUE_COLOR_THEME_LIGHT
                    GamepadColorTheme.DARK -> VALUE_COLOR_THEME_DARK
                }
            sharedPreferences.edit { putInt(KEY_COLOR_THEME, theme) }
            return defaultTheme.colorTheme
        }
    }

    private fun setGamepadSize(gamepadSize: GamepadSize) {
        val size =
            when (gamepadSize) {
                GamepadSize.LARGE -> VALUE_SIZE_LARGE
                GamepadSize.MEDIUM -> VALUE_SIZE_MEDIUM
                GamepadSize.SMALL -> VALUE_SIZE_SMALL
            }
        sharedPreferences.edit { putInt(KEY_SIZE, size) }
    }

    private fun getGamepadSize(): GamepadSize {
        if (sharedPreferences.contains(KEY_SIZE)) {
            val value = sharedPreferences.getInt(KEY_SIZE, VALUE_SIZE_MEDIUM)
            return when (value) {
                VALUE_SIZE_SMALL -> GamepadSize.SMALL
                VALUE_SIZE_MEDIUM -> GamepadSize.MEDIUM
                VALUE_SIZE_LARGE -> GamepadSize.LARGE
                else -> defaultTheme.size
            }
        } else {
            val size =
                when (defaultTheme.size) {
                    GamepadSize.LARGE -> VALUE_SIZE_LARGE
                    GamepadSize.MEDIUM -> VALUE_SIZE_MEDIUM
                    GamepadSize.SMALL -> VALUE_SIZE_SMALL
                }
            sharedPreferences.edit { putInt(KEY_SIZE, size) }
            return defaultTheme.size
        }
    }

    private fun setGamepadStyle(gamepadStyle: GamepadStyle) {
        val style =
            when (gamepadStyle) {
                GamepadStyle.INLINE -> VALUE_STYLE_INLINE
                GamepadStyle.INVERTED -> VALUE_STYLE_INVERTED
            }
        sharedPreferences.edit { putInt(KEY_STYLE, style) }
    }

    private fun getGamepadStyle(): GamepadStyle {
        if (sharedPreferences.contains(KEY_STYLE)) {
            val value = sharedPreferences.getInt(KEY_STYLE, VALUE_STYLE_INLINE)
            return when (value) {
                VALUE_STYLE_INLINE -> GamepadStyle.INLINE
                VALUE_STYLE_INVERTED -> GamepadStyle.INVERTED
                else -> defaultTheme.style
            }
        } else {
            val style =
                when (defaultTheme.style) {
                    GamepadStyle.INLINE -> VALUE_STYLE_INLINE
                    GamepadStyle.INVERTED -> VALUE_STYLE_INVERTED
                }
            sharedPreferences.edit { putInt(KEY_STYLE, style) }
            return defaultTheme.style
        }
    }

    companion object {
        private const val KEY_HAPTICS_ENABLED = "com.android.virtualgamepad.KEY_HAPTICS_ENABLED"
        private const val KEY_COLOR_THEME = "com.android.virtualgamepad.KEY_COLOR_THEME"
        private const val VALUE_COLOR_THEME_LIGHT = 0
        private const val VALUE_COLOR_THEME_DARK = 1
        private const val KEY_SIZE = "com.android.virtualgamepad.KEY_SIZE"
        private const val VALUE_SIZE_SMALL = 0
        private const val VALUE_SIZE_MEDIUM = 1
        private const val VALUE_SIZE_LARGE = 2
        private const val KEY_STYLE = "com.android.virtualgamepad.KEY_STYLE"
        private const val VALUE_STYLE_INLINE = 0
        private const val VALUE_STYLE_INVERTED = 1
    }
}
