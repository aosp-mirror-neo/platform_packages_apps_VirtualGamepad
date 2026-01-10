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

package com.android.virtualgamepad.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.LocalGamepadTheme

@Composable
fun FourButtonCross(
    modifier: Modifier,
    topButton: GameButtonType,
    leftButton: GameButtonType,
    rightButton: GameButtonType,
    bottomButton: GameButtonType,
) {
    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val theme = LocalGamepadTheme.current
    val roundButtonSize = theme.getRoundButtonSize(screenWidthDp)
    // The cross is is like a 3x3 grid where the buttons are in the top/bottom/left/right positions.
    val dpadWidth = theme.getDpadWidth(screenWidthDp)
    val dpadHeight = theme.getDpadHeight(screenWidthDp)
    Box(
        modifier = modifier.width(dpadWidth.dp).height(dpadHeight.dp),
        contentAlignment = Alignment.Center,
    ) {
        GameButton(
            topButton,
            modifier =
                Modifier.align(Alignment.TopCenter)
                    .width(roundButtonSize.dp)
                    .height(roundButtonSize.dp),
        )

        GameButton(
            bottomButton,
            modifier = Modifier.align(Alignment.BottomCenter).size(roundButtonSize.dp),
        )

        GameButton(
            leftButton,
            modifier = Modifier.align(AbsoluteAlignment.CenterLeft).size(roundButtonSize.dp),
        )

        GameButton(
            rightButton,
            modifier = Modifier.align(AbsoluteAlignment.CenterRight).size(roundButtonSize.dp),
        )
    }
}
