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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.ui.LocalGamepadTheme
import com.android.virtualgamepad.ui.theme.Black1D
import com.android.virtualgamepad.ui.theme.Black23
import com.android.virtualgamepad.ui.theme.GamepadTheme
import com.android.virtualgamepad.ui.theme.Gray4A
import com.android.virtualgamepad.ui.theme.Gray83
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

/**
 * A view that matches the appearance of stick.png using two [GradientCircle]s. Adapts its colors
 * based on the current [GamepadTheme].
 */
@Composable
fun StickView(modifier: Modifier = Modifier) {
    val theme = LocalGamepadTheme.current
    val isLight = theme.isLightTheme()

    val outerColors = if (isLight) listOf(Gray83, Gray4A) else listOf(Gray83, Black23)
    val innerColors = if (isLight) listOf(Gray83, Gray4A) else listOf(Gray4A, Black1D)

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Outer circle (the bevel/ring)
        GradientCircle(
            colors = outerColors,
            startPoint = Offset(-0.6f, -0.6f),
            colorStops = listOf(0.0f, 0.4f),
            outlineColor = if (!isLight) com.android.virtualgamepad.ui.theme.Black00 else null,
            outlineWidth = if (!isLight) 4.dp else 0.dp,
            modifier = Modifier.fillMaxSize(0.9f),
        )
        // Inner circle (the recessed top)
        GradientCircle(
            colors = innerColors,
            startPoint = Offset(1f, 1f),
            endPoint = Offset(-1f, -1f),
            colorStops = listOf(0.0f, 0.4f),
            gradientType = GradientType.LINEAR,
            modifier = Modifier.fillMaxSize(0.74f),
        )
    }
}

@Preview
@Composable
fun StickViewLightPreview() {
    CompositionLocalProvider(LocalGamepadTheme provides GamepadTheme.MEDIUM_LIGHT) {
        VirtualGamepadTheme(darkTheme = false) {
            Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                StickView(modifier = Modifier.size(100.dp))
            }
        }
    }
}

@Preview
@Composable
fun StickViewDarkPreview() {
    CompositionLocalProvider(LocalGamepadTheme provides GamepadTheme.MEDIUM_DARK) {
        VirtualGamepadTheme(darkTheme = true) {
            Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                StickView(modifier = Modifier.size(100.dp))
            }
        }
    }
}
