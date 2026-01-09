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

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.android.virtualgamepad.ui.LocalGamepadTheme

@Composable
fun GamepadBackground() {

    val theme = LocalGamepadTheme.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize().background(theme.backgroundColor)) {
        val numberOfSlats = (maxHeight / 16).value.toInt()
        Column() {
            (0..numberOfSlats).forEach {
                Spacer(modifier = Modifier.weight(.75f))
                Box(
                    modifier =
                        Modifier.fillMaxSize()
                            .background(
                                brush =
                                    Brush.verticalGradient(
                                        colors = listOf(theme.slatColor, theme.backgroundColor)
                                    )
                            )
                            .weight(.25f)
                )
            }
            Spacer(modifier = Modifier.weight(.75f))
        }
    }
}
