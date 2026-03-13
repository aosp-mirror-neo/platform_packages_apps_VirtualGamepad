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

package com.android.virtualgamepad.backend

import android.app.Activity
import android.hardware.input.InputManager
import android.hardware.input.VirtualGamepad
import android.hardware.input.VirtualGamepadConfig
import android.hardware.input.VirtualGamepadMotionEvent
import android.hardware.input.VirtualGamepadMotionEventBuilder
import android.hardware.input.VirtualKeyEvent
import android.util.Log
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import com.android.virtualgamepad.model.GameButtonType
import com.android.virtualgamepad.model.GameButtonType.DPAD_DOWN
import com.android.virtualgamepad.model.GameButtonType.DPAD_LEFT
import com.android.virtualgamepad.model.GameButtonType.DPAD_RIGHT
import com.android.virtualgamepad.model.GameButtonType.DPAD_UP
import com.android.virtualgamepad.model.GameButtonType.L2
import com.android.virtualgamepad.model.GameButtonType.R2

/** A gamepad backend that calls the InputManager to create a virtual gamepad. */
class LocalGamepadBackend : GamepadBackend {
    /** A data class that tracks the state of the gamepad's triggers and joysticks. */
    private data class GamepadState(
        var x: Float = 0.0f,
        var y: Float = 0.0f,
        var z: Float = 0.0f,
        var rz: Float = 0.0f,
        var hatX: Float = 0.0f,
        var hatY: Float = 0.0f,
        var lTrigger: Float = 0.0f,
        var rTrigger: Float = 0.0f,
    )

    /**
     * Track the individual press state of dpad buttons to calculate the combined hatX and hatY axis
     * values for diagonal movement and release.
     */
    private data class DpadState(
        var up: Boolean = false,
        var down: Boolean = false,
        var left: Boolean = false,
        var right: Boolean = false,
    ) {
        fun getHatX(): Float {
            return when {
                left -> -1.0f
                right -> 1.0f
                else -> 0.0f
            }
        }

        fun getHatY(): Float {
            return when {
                up -> -1.0f
                down -> 1.0f
                else -> 0.0f
            }
        }
    }

    private val gamepadState = GamepadState()
    private val dpadState = DpadState()

    override fun onLeftJoystick(x: Float, y: Float) {
        val previousState = gamepadState.copy()
        val eventTimeNanos = System.nanoTime()
        gamepadState.x = x
        gamepadState.y = y
        if (gamepadState != previousState) {
            gamepad?.sendMotionEvent(
                synthesizeGamepadMotionEvent(gamepadState, previousState, eventTimeNanos)
            )
        }
    }

    override fun onRightJoystick(z: Float, rz: Float) {
        val previousState = gamepadState.copy()
        val eventTimeNanos = System.nanoTime()
        gamepadState.z = z
        gamepadState.rz = rz
        if (gamepadState != previousState) {
            gamepad?.sendMotionEvent(
                synthesizeGamepadMotionEvent(gamepadState, previousState, eventTimeNanos)
            )
        }
    }

    override fun onButtonDown(gameButtonType: GameButtonType) {
        onButtonAction(gameButtonType, ACTION_DOWN)
    }

    override fun onButtonUp(gameButtonType: GameButtonType) {
        onButtonAction(gameButtonType, ACTION_UP)
    }

    private fun onButtonAction(gameButtonType: GameButtonType, action: Int) {
        val previousState = gamepadState.copy()
        val eventTimeNanos = System.nanoTime()

        when (gameButtonType) {
            DPAD_UP,
            DPAD_DOWN,
            DPAD_LEFT,
            DPAD_RIGHT -> {
                // Special handling for dpad buttons using hatX and hatY
                when (action) {
                    ACTION_DOWN ->
                        when (gameButtonType) {
                            DPAD_UP -> dpadState.up = true
                            DPAD_DOWN -> dpadState.down = true
                            DPAD_LEFT -> dpadState.left = true
                            DPAD_RIGHT -> dpadState.right = true
                            else -> {}
                        }

                    ACTION_UP ->
                        when (gameButtonType) {
                            DPAD_UP -> dpadState.up = false
                            DPAD_DOWN -> dpadState.down = false
                            DPAD_LEFT -> dpadState.left = false
                            DPAD_RIGHT -> dpadState.right = false
                            else -> {}
                        }
                }
                gamepadState.hatX = dpadState.getHatX()
                gamepadState.hatY = dpadState.getHatY()
            }

            // Update gamepad state for L2 and R2 triggers
            L2,
            R2 -> {
                when (action) {
                    ACTION_DOWN ->
                        when (gameButtonType) {
                            L2 -> gamepadState.lTrigger = 1.0f
                            R2 -> gamepadState.rTrigger = 1.0f
                            else -> {}
                        }

                    ACTION_UP ->
                        when (gameButtonType) {
                            L2 -> gamepadState.lTrigger = 0.0f
                            R2 -> gamepadState.rTrigger = 0.0f
                            else -> {}
                        }
                }
            }
            // No special handling for other buttons.
            else -> {}
        }

        when (gameButtonType) {
            // DPad only sends motion events.
            DPAD_UP,
            DPAD_DOWN,
            DPAD_LEFT,
            DPAD_RIGHT -> {
                if (gamepadState != previousState) {
                    gamepad?.sendMotionEvent(
                        synthesizeGamepadMotionEvent(gamepadState, previousState, eventTimeNanos)
                    )
                }
            }
            // L2 and R2 send both motion events and key events.
            L2,
            R2 -> {
                if (gamepadState != previousState) {
                    gamepad?.sendMotionEvent(
                        synthesizeGamepadMotionEvent(gamepadState, previousState, eventTimeNanos)
                    )
                }
                val keyEvent =
                    VirtualKeyEvent.Builder()
                        .setKeyCode(gameButtonType.keyCode)
                        .setAction(action)
                        .build()
                gamepad?.sendKeyEvent(keyEvent)
            }
            // Other buttons send only key events.
            else -> {
                val keyEvent =
                    VirtualKeyEvent.Builder()
                        .setKeyCode(gameButtonType.keyCode)
                        .setAction(action)
                        .build()
                gamepad?.sendKeyEvent(keyEvent)
            }
        }
    }

    override fun connect(context: Activity) {
        synchronized(lock) {
            if (refCount == 0) {
                val inputManager = context.getSystemService(InputManager::class.java)
                val config = VirtualGamepadConfig()

                // TODO(jiamingliu) update these values
                config.name = "Android Virtual Gamepad"
                config.vendorId = 1234
                config.productId = 5678

                config.associatedDisplayId = context.displayId
                config.registerTriggerAxes = true
                gamepad = inputManager.createVirtualGamepad(config)
                Log.i(TAG, "Gamepad initialized")
            }
            refCount++
        }
    }

    override fun disconnect() {
        synchronized(lock) {
            if (refCount > 0) {
                refCount--
                if (refCount == 0) {
                    gamepad?.close()
                    gamepad = null
                    Log.i(TAG, "Gamepad destroyed")
                }
            }
        }
    }

    private fun synthesizeGamepadMotionEvent(
        currentState: GamepadState,
        previousState: GamepadState,
        eventTimeNanos: Long,
    ): VirtualGamepadMotionEvent {
        val builder = VirtualGamepadMotionEventBuilder().setEventTimeNanos(eventTimeNanos)
        if (currentState.x != previousState.x) {
            builder.setX(currentState.x)
        }
        if (currentState.y != previousState.y) {
            builder.setY(currentState.y)
        }
        if (currentState.z != previousState.z) {
            builder.setZ(currentState.z)
        }
        if (currentState.rz != previousState.rz) {
            builder.setRz(currentState.rz)
        }
        if (currentState.hatX != previousState.hatX) {
            builder.setHatX(currentState.hatX)
        }
        if (currentState.hatY != previousState.hatY) {
            builder.setHatY(currentState.hatY)
        }
        if (currentState.lTrigger != previousState.lTrigger) {
            builder.setLTrigger(currentState.lTrigger)
        }
        if (currentState.rTrigger != previousState.rTrigger) {
            builder.setRTrigger(currentState.rTrigger)
        }
        return builder.build()
    }

    companion object {
        private const val TAG = "LocalGamepadBackend"
        private val lock = Any()
        private var gamepad: VirtualGamepad? = null
        private var refCount = 0
    }
}
