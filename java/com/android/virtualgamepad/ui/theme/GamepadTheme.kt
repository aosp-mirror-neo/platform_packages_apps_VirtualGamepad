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
import android.annotation.StringRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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
    val isHapticsEnabled: Boolean = true,
    val size: GamepadSize = GamepadSize.MEDIUM,
    val style: GamepadStyle = GamepadStyle.INLINE,
    val colorTheme: GamepadColorTheme = GamepadColorTheme.LIGHT,
) {

    fun copy(
        isHapticsEnabled: Boolean = this.isHapticsEnabled,
        size: GamepadSize = this.size,
        style: GamepadStyle = this.style,
        colorTheme: GamepadColorTheme = this.colorTheme,
    ): GamepadTheme = GamepadTheme(isHapticsEnabled, size, style, colorTheme)

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

    fun dialogTitleStyle(): TextStyle {
        val color =
            if (isLightTheme()) {
                Color(0xFF313332)
            } else {
                Color(0xFFD1D2CF)
            }
        return TextStyle(
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight(500),
            color = color,
        )
    }

    fun dialogSubtitleStyle(): TextStyle {
        return TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            color = Color(0xFF919390),
        )
    }

    fun dialogColor(): Color {
        return if (isLightTheme()) {
            Color(0xFFFFFFFF)
        } else {
            Color(0xFF000000)
        }
    }

    fun dialogIconTint(): Color {
        return if (isLightTheme()) {
            Grey31
        } else {
            GreyBF
        }
    }

    fun dialogHeaderStyle(): TextStyle {
        val color =
            if (isLightTheme()) {
                Color(0xFF313332)
            } else {
                Color(0xFFBFC9C3)
            }
        return TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(400),
            color = color,
        )
    }

    fun dropdownItemTint(): Color {
        return if (isLightTheme()) {
            Color(0xFF313332)
        } else {
            Color(0xFFBFC9C3)
        }
    }

    fun dropDownItemStyle(): TextStyle {
        val color =
            if (isLightTheme()) {
                Color(0xFF313332)
            } else {
                Color(0xFFBFC9C3)
            }
        return TextStyle(
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight(500),
            color = color,
        )
    }

    fun dropdownColor(): Color {
        return Color(0xFF2A2D2B)
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
    fun settingsButtonPressedResource(): Int {
        return if (isLightTheme()) {
            R.drawable.button_pressed_settings
        } else {
            R.drawable.button_pressed_settings_dark
        }
    }

    @DrawableRes
    fun settingsButtonDescription(): Int {
        return R.string.description_game_button_settings
    }

    @StringRes
    fun gameButtonDescription(gameButtonType: GameButtonType): Int {
        when (gameButtonType) {
            GameButtonType.A -> return R.string.description_game_button_a
            GameButtonType.B -> return R.string.description_game_button_b
            GameButtonType.X -> return R.string.description_game_button_x
            GameButtonType.Y -> return R.string.description_game_button_y
            GameButtonType.L1 -> return R.string.description_game_button_l1
            GameButtonType.L2 -> return R.string.description_game_button_l2
            GameButtonType.L3 -> return R.string.description_game_button_l3
            GameButtonType.R1 -> return R.string.description_game_button_r1
            GameButtonType.R2 -> return R.string.description_game_button_r2
            GameButtonType.R3 -> return R.string.description_game_button_r3
            GameButtonType.DPAD_UP -> return R.string.description_game_button_dpad_up
            GameButtonType.DPAD_DOWN -> return R.string.description_game_button_dpad_down
            GameButtonType.DPAD_LEFT -> return R.string.description_game_button_dpad_left
            GameButtonType.DPAD_RIGHT -> return R.string.description_game_button_dpad_right
            GameButtonType.Menu -> return R.string.description_game_button_menu
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

    @DrawableRes
    fun gameButtonPressedResource(gameButtonType: GameButtonType): Int {
        val isLightTheme = isLightTheme()
        return when (gameButtonType) {
            GameButtonType.A ->
                if (isLightTheme) {
                    R.drawable.button_pressed_c_a
                } else {
                    R.drawable.button_pressed_c_a_dark
                }
            GameButtonType.B ->
                if (isLightTheme) {
                    R.drawable.button_pressed_c_b
                } else {
                    R.drawable.button_pressed_c_b_dark
                }
            GameButtonType.X ->
                if (isLightTheme) {
                    R.drawable.button_pressed_c_x
                } else {
                    R.drawable.button_pressed_c_x_dark
                }
            GameButtonType.Y ->
                if (isLightTheme) {
                    R.drawable.button_pressed_c_y
                } else {
                    R.drawable.button_pressed_c_y_dark
                }
            GameButtonType.L1 ->
                if (isLightTheme) {
                    R.drawable.button_pressed_l1
                } else {
                    R.drawable.button_pressed_l1_dark
                }
            GameButtonType.L2 ->
                if (isLightTheme) {
                    R.drawable.button_pressed_l2
                } else {
                    R.drawable.button_pressed_l2_dark
                }
            GameButtonType.L3 ->
                if (isLightTheme) {
                    R.drawable.button_pressed_l3
                } else {
                    R.drawable.button_pressed_l3_dark
                }
            GameButtonType.R1 ->
                if (isLightTheme) {
                    R.drawable.button_pressed_r1
                } else {
                    R.drawable.button_pressed_r1_dark
                }
            GameButtonType.R2 ->
                if (isLightTheme) {
                    R.drawable.button_pressed_r2
                } else {
                    R.drawable.button_pressed_r2_dark
                }
            GameButtonType.R3 ->
                if (isLightTheme) {
                    R.drawable.button_pressed_r3
                } else {
                    R.drawable.button_pressed_r3_dark
                }
            GameButtonType.DPAD_UP ->
                if (isLightTheme) {
                    R.drawable.button_pressed_d_u
                } else {
                    R.drawable.button_pressed_d_u_dark
                }
            GameButtonType.DPAD_DOWN ->
                if (isLightTheme) {
                    R.drawable.button_pressed_d_d
                } else {
                    R.drawable.button_pressed_d_d_dark
                }
            GameButtonType.DPAD_LEFT ->
                if (isLightTheme) {
                    R.drawable.button_pressed_d_l
                } else {
                    R.drawable.button_pressed_d_l_dark
                }
            GameButtonType.DPAD_RIGHT ->
                if (isLightTheme) {
                    R.drawable.button_pressed_d_r
                } else {
                    R.drawable.button_pressed_d_r_dark
                }
            GameButtonType.Menu ->
                if (isLightTheme) {
                    R.drawable.button_pressed_menu
                } else {
                    R.drawable.button_pressed_menu_dark
                }
        }
    }

    companion object {
        val LARGE_LIGHT =
            GamepadTheme(
                isHapticsEnabled = true,
                GamepadSize.LARGE,
                GamepadStyle.INLINE,
                GamepadColorTheme.LIGHT,
            )
        val LARGE_DARK =
            GamepadTheme(
                isHapticsEnabled = true,
                GamepadSize.LARGE,
                GamepadStyle.INLINE,
                GamepadColorTheme.DARK,
            )
        val MEDIUM_LIGHT =
            GamepadTheme(
                isHapticsEnabled = true,
                GamepadSize.MEDIUM,
                GamepadStyle.INLINE,
                GamepadColorTheme.LIGHT,
            )
        val MEDIUM_DARK =
            GamepadTheme(
                isHapticsEnabled = true,
                GamepadSize.MEDIUM,
                GamepadStyle.INLINE,
                GamepadColorTheme.DARK,
            )
        val SMALL_LIGHT =
            GamepadTheme(
                isHapticsEnabled = true,
                GamepadSize.SMALL,
                GamepadStyle.INLINE,
                GamepadColorTheme.LIGHT,
            )
        val SMALL_DARK =
            GamepadTheme(
                isHapticsEnabled = true,
                GamepadSize.SMALL,
                GamepadStyle.INLINE,
                GamepadColorTheme.DARK,
            )
    }
}
