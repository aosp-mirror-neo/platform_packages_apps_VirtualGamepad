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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.android.virtualgamepad.ui.LocalGamepadTheme
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

    val outerColors =
        if (isLight) listOf(Gray83, Gray4A)
        else listOf(Color(0xFFE0E0E0), Color(0xFFCBCBCB), Color(0xFF979797), Color(0xFF6C6C6C))
    val innerColors =
        if (isLight) listOf(Gray83, Gray4A)
        else listOf(Color(0xFF9E9E9E), Color(0xFF8D8D8C), Color(0xFF6F6F6F))
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        // Outer circle (the bevel/ring)
        GradientCircle(
            colors = outerColors,
            startPoint = if (isLight) Offset(-0.6f, -0.6f) else Offset(-0.3f, -0.3f),
            colorStops = if (isLight) listOf(0.0f, 0.4f) else listOf(0.0f, 0.33f, 0.66f, 1.0f),
            gradientType = GradientType.RADIAL,
            outlineColor = null,
            outlineWidth = 0.dp,
            modifier = Modifier.fillMaxSize(0.9f),
        ) // Inner circle (the recessed top)
        GradientCircle(
            colors = innerColors,
            startPoint = Offset(1f, 1f),
            endPoint = Offset(-1f, -1f),
            colorStops = if (isLight) listOf(0.0f, 0.4f) else listOf(0.0f, 0.2f, 0.4f),
            gradientType = GradientType.LINEAR,
            modifier = Modifier.fillMaxSize(0.74f),
        )
    }
}

@Preview(showBackground = true, widthDp = 200, heightDp = 200)
@Composable
fun StickViewPreview() {
    VirtualGamepadTheme {
        Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
            StickView(modifier = Modifier.size(100.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 200, heightDp = 200)
@Composable
fun StickViewDarkPreview() {
    val gamepadTheme = GamepadTheme.MEDIUM_DARK
    VirtualGamepadTheme(darkTheme = true) {
        Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
            StickView(modifier = Modifier.size(100.dp))
        }
    }
}
