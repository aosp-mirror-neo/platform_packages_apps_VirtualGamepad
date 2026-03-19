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

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class GradientType {
    RADIAL,
    LINEAR,
}

/**
 * A circle that has a gradient that starts at a specified point in the circle.
 *
 * @param colors The colors to be used in the gradient.
 * @param startPoint The center of the radial gradient (for RADIAL), start point (for LINEAR), or
 *   center of the sweep gradient (for CONIC) in unit coordinates. Offset(0,0) is the center of the
 *   circle.
 * @param endPoint The end point for LINEAR gradient in unit coordinates.
 * @param colorStops The color stops for the gradient.
 * @param gradientType The type of gradient to use (RADIAL, LINEAR, or CONIC).
 * @param modifier The modifier to be applied to the component.
 */
@Composable
fun GradientCircle(
    colors: List<Color>,
    startPoint: Offset = Offset.Zero,
    endPoint: Offset = Offset.Zero,
    colorStops: List<Float>? = null,
    gradientType: GradientType = GradientType.RADIAL,
    outlineColor: Color? = null,
    outlineWidth: Dp = 0.dp,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier) {
        val canvasCenter = center
        val radius = size.minDimension / 2

        // Calculate the absolute coordinates from unit coordinates
        val startPointPx =
            Offset(
                x = canvasCenter.x + (startPoint.x * radius),
                y = canvasCenter.y + (startPoint.y * radius),
            )
        val endPointPx =
            Offset(
                x = canvasCenter.x + (endPoint.x * radius),
                y = canvasCenter.y + (endPoint.y * radius),
            )

        val brush =
            when (gradientType) {
                GradientType.RADIAL -> {
                    if (colorStops != null && colorStops.size == colors.size) {
                        Brush.radialGradient(
                            colorStops = colorStops.zip(colors).toTypedArray(),
                            center = startPointPx,
                            radius = radius,
                        )
                    } else {
                        Brush.radialGradient(
                            colors = colors,
                            center = startPointPx,
                            radius = radius,
                        )
                    }
                }
                GradientType.LINEAR -> {
                    if (colorStops != null && colorStops.size == colors.size) {
                        Brush.linearGradient(
                            colorStops = colorStops.zip(colors).toTypedArray(),
                            start = startPointPx,
                            end = endPointPx,
                        )
                    } else {
                        Brush.linearGradient(
                            colors = colors,
                            start = startPointPx,
                            end = endPointPx,
                        )
                    }
                }
            }

        drawCircle(brush = brush, radius = radius, center = canvasCenter)

        val outlineWidthPx = outlineWidth.toPx()
        if (outlineColor != null && outlineWidthPx > 0) {
            drawCircle(
                color = outlineColor,
                radius = radius,
                center = canvasCenter,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = outlineWidthPx),
            )
        }
    }
}

@Preview
@Composable
fun GradientCirclePreview() {
    GradientCircle(
        colors = listOf(Color.White, Color.Blue),
        startPoint = Offset(0.5f, -0.5f),
        modifier = Modifier.size(100.dp),
    )
}
