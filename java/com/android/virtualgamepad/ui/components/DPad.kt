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

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.ui.GamepadLayoutSpec
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

@Composable
fun DPad(modifier: Modifier) {
    val spec = GamepadLayoutSpec()

    val screenWidthDp = LocalConfiguration.current.screenWidthDp
    val theme = LocalGamepadTheme.current
    val roundButtonSize = theme.getRoundButtonSize(screenWidthDp)
    // The cross is is like a 3x3 grid where the buttons are in the top/bottom/left/right positions.
    val dpadWidth = theme.getDpadWidth(screenWidthDp)
    val dpadHeight = theme.getDpadHeight(screenWidthDp)

    val constraintSet =
        spec.computeFourButtonCrossConstraintSet(
            topButtonId = GamepadLayoutSpec.dpadUpId,
            leftButtonId = GamepadLayoutSpec.dpadLeftId,
            rightButtonId = GamepadLayoutSpec.dpadRightId,
            bottomButtonId = GamepadLayoutSpec.dpadDownId,
        )

    ConstraintLayout(
        constraintSet = constraintSet,
        modifier = modifier.width(dpadWidth.dp).height(dpadHeight.dp),
    ) {
        GameButton(
            GameButtonType.DPAD_UP,
            modifier = Modifier.size(roundButtonSize.dp).layoutId(GamepadLayoutSpec.dpadUpId),
        )

        GameButton(
            GameButtonType.DPAD_DOWN,
            modifier = Modifier.size(roundButtonSize.dp).layoutId(GamepadLayoutSpec.dpadDownId),
        )

        GameButton(
            GameButtonType.DPAD_LEFT,
            modifier = Modifier.size(roundButtonSize.dp).layoutId(GamepadLayoutSpec.dpadLeftId),
        )

        GameButton(
            GameButtonType.DPAD_RIGHT,
            modifier = Modifier.size(roundButtonSize.dp).layoutId(GamepadLayoutSpec.dpadRightId),
        )
    }
}

@Preview(showBackground = true, widthDp = 189, heightDp = 189)
@Composable
fun GamepadPreview() {
    VirtualGamepadTheme { DPad(modifier = Modifier.size(96.dp)) }
}
