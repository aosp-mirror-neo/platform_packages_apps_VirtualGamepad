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
import androidx.compose.ui.unit.IntOffset
import com.android.virtualgamepad.R
import com.android.virtualgamepad.model.GameButtonType
import kotlin.math.sqrt

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

// The design is based on the width of 883dp. If the width is smaller, UI needs to be scaled.
private const val sizeBase = 883

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

    // The design is based the width of 883dp. If the width is smaller, UI needs to be scaled.
    private fun Int.adjust(screenWidthDp: Int): Int {
        if (screenWidthDp >= sizeBase) return this
        return this * screenWidthDp / sizeBase
    }

    private fun Float.adjust(screenWidthDp: Int): Float {
        if (screenWidthDp >= sizeBase) return this
        return this * screenWidthDp / sizeBase
    }

    private fun IntOffset.adjust(screenWidthDp: Int): IntOffset {
        if (screenWidthDp >= sizeBase) return this
        return this * screenWidthDp.toFloat() / sizeBase.toFloat()
    }

    fun getRoundButtonSize(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> 70
            GamepadSize.MEDIUM -> 54
            GamepadSize.SMALL -> 52
        }.adjust(screenWidthDp)

    fun getSquareButtonSize(screenWidthDp: Int): Int =
        when (size) {
            GamepadSize.LARGE -> 70
            GamepadSize.MEDIUM -> 66
            GamepadSize.SMALL -> 64
        }.adjust(screenWidthDp)

    fun getFunctionButtonWidth(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> 72
            GamepadSize.MEDIUM -> 72
            GamepadSize.SMALL -> 72
        }.adjust(screenWidthDp)

    fun getFunctionButtonHeight(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> 48
            GamepadSize.MEDIUM -> 48
            GamepadSize.SMALL -> 48
        }.adjust(screenWidthDp)

    fun getOffsetFunctionButtons(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> IntOffset(x = 295, y = 44)
            GamepadSize.MEDIUM -> IntOffset(x = 295, y = 44)
            GamepadSize.SMALL -> IntOffset(x = 295, y = 54)
        }.adjust(screenWidthDp)

    fun getFourButtonBoxSize(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> IntOffset(x = 189, y = 208)
            GamepadSize.MEDIUM -> IntOffset(x = 153, y = 152)
            GamepadSize.SMALL -> IntOffset(x = 151, y = 150)
        }.adjust(screenWidthDp)

    fun getShoulderButtonBox(screenWidthDp: Int) =
        when (size) {
            // These are calculated based on the spacing between shoulder buttons
            GamepadSize.LARGE -> 70 * sqrt(2f) + 59
            GamepadSize.MEDIUM -> 66 * sqrt(2f) + 50
            GamepadSize.SMALL -> 64 * sqrt(2f) + 50
        }.adjust(screenWidthDp)

    fun getJoystickBox(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> 161
            GamepadSize.MEDIUM -> 156
            GamepadSize.SMALL -> 156
        }.adjust(screenWidthDp)

    fun getJoystickOuterRingSize(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> 93
            GamepadSize.MEDIUM -> 88
            GamepadSize.SMALL -> 88
        }.adjust(screenWidthDp)

    fun getJoystickInnerRingSize(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> 78
            GamepadSize.MEDIUM -> 73
            GamepadSize.SMALL -> 73
        }.adjust(screenWidthDp)

    fun getOffsetFourButtonSet(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> IntOffset(x = 20, y = 87)
            GamepadSize.MEDIUM -> IntOffset(x = 30, y = 123)
            GamepadSize.SMALL -> IntOffset(x = 30, y = 97)
        }.adjust(screenWidthDp)

    fun getOffsetShoulderButtonSet(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> IntOffset(x = 133, y = 212)
            GamepadSize.MEDIUM -> IntOffset(x = 128, y = 220)
            GamepadSize.SMALL -> IntOffset(x = 128, y = 191)
        }.adjust(screenWidthDp)

    fun getOffsetShoulder3(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> IntOffset(x = 363, y = 187)
            GamepadSize.MEDIUM -> IntOffset(x = 348, y = 166)
            GamepadSize.SMALL -> IntOffset(x = 351, y = 130)
        }.adjust(screenWidthDp)

    fun getOffsetJoystick(screenWidthDp: Int) =
        when (size) {
            GamepadSize.LARGE -> IntOffset(x = 215, y = 40)
            GamepadSize.MEDIUM -> IntOffset(x = 185, y = 45)
            GamepadSize.SMALL -> IntOffset(x = 185, y = 15)
        }.adjust(screenWidthDp)

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

    val labelTextColor =
        when (colorTheme) {
            GamepadColorTheme.LIGHT -> Black00
            GamepadColorTheme.DARK -> WhiteEC
        }

    val joystickOuterRingGradient = listOf(Gray83, Black1D)
    val joystickInnerRingGradient = listOf(Gray4A, Black22)

    fun isLightTheme(): Boolean {
        return colorTheme == GamepadColorTheme.LIGHT
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
