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

package com.android.virtualgamepad.ui.theme

import android.annotation.DrawableRes
import com.android.virtualgamepad.R
import com.android.virtualgamepad.model.GameButtonType

enum class GamepadSize {
    LARGE,
    MEDIUM,
    SMALL,
}

enum class GamepadColorTheme {
    LIGHT,
    DARK,
}

enum class GamepadStyle {
    INLINE,
    INVERTED,
}

class GamepadTheme(
    val size: GamepadSize = GamepadSize.MEDIUM,
    val style: GamepadStyle = GamepadStyle.INLINE,
    val colorTheme: GamepadColorTheme = GamepadColorTheme.LIGHT,
) {

    fun copy(
        size: GamepadSize = this.size,
        style: GamepadStyle = this.style,
        colorTheme: GamepadColorTheme = this.colorTheme,
    ): GamepadTheme = GamepadTheme(size, style, colorTheme)

    fun getLeftRightSpacerWidth() = 32

    val backgroundColor =
        when (colorTheme) {
            GamepadColorTheme.LIGHT -> BackgroundBaseColorLight
            GamepadColorTheme.DARK -> BackgroundBaseColorDark
        }

    val slatColor =
        when (colorTheme) {
            GamepadColorTheme.LIGHT -> SlatColorLight
            GamepadColorTheme.DARK -> SlatColorDark
        }

    val joystickBackgroundColor =
        when (colorTheme) {
            GamepadColorTheme.LIGHT -> GrayD0
            GamepadColorTheme.DARK -> Black23
        }

    val panelColors =
        when (colorTheme) {
            GamepadColorTheme.LIGHT -> listOf(PanelColorLightStart, PanelColorLightEnd)
            GamepadColorTheme.DARK -> listOf(PanelColorDarkStart, PanelColorDarkEnd)
        }

    fun isLightTheme(): Boolean {
        return colorTheme == GamepadColorTheme.LIGHT
    }

    @DrawableRes
    fun getJoystickResource(): Int {
        return if (isLightTheme()) {
            R.drawable.stick
        } else {
            R.drawable.stick_dark
        }
    }

    @DrawableRes
    fun settingsButtonResource(): Int {
        return if (isLightTheme()) {
            R.drawable.button_settings
        } else {
            R.drawable.button_settings_dark
        }
    }

    @DrawableRes
    fun gameButtonResource(gameButtonType: GameButtonType): Int {
        val isLightTheme = isLightTheme()
        return when (gameButtonType) {
            GameButtonType.A ->
                if (isLightTheme) {
                    R.drawable.button_c_a
                } else {
                    R.drawable.button_c_a_dark
                }
            GameButtonType.B ->
                if (isLightTheme) {
                    R.drawable.button_c_b
                } else {
                    R.drawable.button_c_b_dark
                }
            GameButtonType.X ->
                if (isLightTheme) {
                    R.drawable.button_c_x
                } else {
                    R.drawable.button_c_x_dark
                }
            GameButtonType.Y ->
                if (isLightTheme) {
                    R.drawable.button_c_y
                } else {
                    R.drawable.button_c_y_dark
                }
            GameButtonType.L1 ->
                if (isLightTheme) {
                    R.drawable.button_l1
                } else {
                    R.drawable.button_l1_dark
                }
            GameButtonType.L2 ->
                if (isLightTheme) {
                    R.drawable.button_l2
                } else {
                    R.drawable.button_l2_dark
                }
            GameButtonType.L3 ->
                if (isLightTheme) {
                    R.drawable.button_l3
                } else {
                    R.drawable.button_l3_dark
                }
            GameButtonType.R1 ->
                if (isLightTheme) {
                    R.drawable.button_r1
                } else {
                    R.drawable.button_r1_dark
                }
            GameButtonType.R2 ->
                if (isLightTheme) {
                    R.drawable.button_r2
                } else {
                    R.drawable.button_r2_dark
                }
            GameButtonType.R3 ->
                if (isLightTheme) {
                    R.drawable.button_r3
                } else {
                    R.drawable.button_r3_dark
                }
            GameButtonType.DPAD_UP ->
                if (isLightTheme) {
                    R.drawable.button_d_u
                } else {
                    R.drawable.button_d_u_dark
                }
            GameButtonType.DPAD_DOWN ->
                if (isLightTheme) {
                    R.drawable.button_d_d
                } else {
                    R.drawable.button_d_d_dark
                }
            GameButtonType.DPAD_LEFT ->
                if (isLightTheme) {
                    R.drawable.button_d_l
                } else {
                    R.drawable.button_d_l_dark
                }
            GameButtonType.DPAD_RIGHT ->
                if (isLightTheme) {
                    R.drawable.button_d_r
                } else {
                    R.drawable.button_d_r_dark
                }
            GameButtonType.Menu ->
                if (isLightTheme) {
                    R.drawable.button_menu
                } else {
                    R.drawable.button_menu_dark
                }
        }
    }

    companion object {
        val LARGE_LIGHT =
            GamepadTheme(GamepadSize.LARGE, GamepadStyle.INLINE, GamepadColorTheme.LIGHT)
        val LARGE_DARK =
            GamepadTheme(GamepadSize.LARGE, GamepadStyle.INLINE, GamepadColorTheme.DARK)
        val MEDIUM_LIGHT =
            GamepadTheme(GamepadSize.MEDIUM, GamepadStyle.INLINE, GamepadColorTheme.LIGHT)
        val MEDIUM_DARK =
            GamepadTheme(GamepadSize.MEDIUM, GamepadStyle.INLINE, GamepadColorTheme.DARK)
        val SMALL_LIGHT =
            GamepadTheme(GamepadSize.SMALL, GamepadStyle.INLINE, GamepadColorTheme.LIGHT)
        val SMALL_DARK =
            GamepadTheme(GamepadSize.SMALL, GamepadStyle.INLINE, GamepadColorTheme.DARK)
    }
}
