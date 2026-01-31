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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.translate
import androidx.graphics.shapes.CornerRounding
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import com.android.virtualgamepad.ui.LocalGamepadTheme

/**
 * Provides the background behind the buttons and joystick. The shape is generally a pentagon with
 * one rounded corner on either the right or the left. The rounded corner has a vertex angle of 45
 * degrees.
 *
 * @param modifier Modifier to be applied to the background.
 * @param isPointOnRight determines if the rounded corner is on the left or right
 * @param cornerX the X coordinate for the rounded corner. The range is approximately [-1, 1], but
 *   it may be necessary to go a bit beyond because the corner is rounded
 * @param cornerX the Y coordinate for the rounded corner. The range is [-1, 1].
 */
@Composable
fun PanelBackground(
    modifier: Modifier,
    isPointOnRight: Boolean,
    rounding: Float,
    cornerX: Float,
    cornerY: Float,
) {
    val gamepadTheme = LocalGamepadTheme.current

    val topY = 2f
    val bottomY = -2f
    val leftX = -1f
    val vertices =
        if (isPointOnRight) {
            floatArrayOf(
                leftX,
                topY,
                cornerY + cornerX - topY,
                topY,
                cornerX,
                cornerY,
                bottomY - cornerY + cornerX,
                bottomY,
                leftX,
                bottomY,
            )
        } else {
            floatArrayOf(
                -leftX,
                topY,
                -cornerY - cornerX + topY,
                topY,
                -cornerX,
                cornerY,
                -bottomY + cornerY - cornerX,
                bottomY,
                -leftX,
                bottomY,
            )
        }

    val rounding =
        listOf(
            CornerRounding.Unrounded,
            CornerRounding.Unrounded,
            CornerRounding(rounding),
            CornerRounding.Unrounded,
            CornerRounding.Unrounded,
        )

    val polygon = RoundedPolygon(vertices = vertices, perVertexRounding = rounding)

    Box(
        modifier =
            modifier.drawWithCache {
                val roundedPolygonPath = polygon.toPath().asComposePath()
                onDrawBehind {
                    scale(size.width * 0.5f, size.width * 0.5f) {
                        translate(size.width * 0.5f, size.height * 0.5f) {
                            drawPath(
                                roundedPolygonPath,
                                brush = Brush.linearGradient(colors = gamepadTheme.panelColors),
                            )
                        }
                    }
                }
            }
    )
}
