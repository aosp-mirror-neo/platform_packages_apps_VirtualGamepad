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

package com.android.virtualgamepad

import android.app.AppGlobals
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.hardware.input.InputManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.os.SystemClock
import android.view.InputDevice
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.Density
import com.android.virtualgamepad.backend.LocalGamepadBackend
import com.android.virtualgamepad.backend.SettingsRepo
import com.android.virtualgamepad.model.GamepadState
import com.android.virtualgamepad.ui.Gamepad
import com.android.virtualgamepad.ui.theme.GamepadTheme
import com.android.virtualgamepad.ui.theme.VirtualGamepadTheme

val LocalGamepadState = compositionLocalOf<GamepadState?> { null }

class GamepadActivity : ComponentActivity(), InputManager.InputDeviceListener {
    companion object {
        const val INVALID_START_TIME_MS = 0L
        const val INVALID_LATENCY_MS = -1L
    }

    val gamepadBackend = LocalGamepadBackend()
    private lateinit var inputManager: InputManager

    // The caller package name provided by the app process. Note that this should NOT be used for
    // anything that has security implications.
    var callerPackageName: String? = null
    var callerUid: Int = Process.INVALID_UID
    var startTime: Long = INVALID_START_TIME_MS
    var shouldLogLatency: Boolean = false
    lateinit var gamepadState: GamepadState
    lateinit var fixedDensity: Density

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inputManager = getSystemService(InputManager::class.java)!!
        enableEdgeToEdge()
        val defaultTheme =
            if (isSystemInDarkTheme()) {
                GamepadTheme.MEDIUM_DARK
            } else {
                GamepadTheme.MEDIUM_LIGHT
            }
        callerPackageName = intent.getStringExtra(Intent.EXTRA_PACKAGE_NAME)
        callerUid = intent.getIntExtra(Intent.EXTRA_UID, Process.INVALID_UID)
        gamepadState = GamepadState(gamepadBackend)
        fixedDensity = Density(2.5f, 1f)

        // only log latency on the first onCreate for this intent, since the startTime is only valid
        // when the activity is created the first time with this Intent. Subsequent onCreate calls
        // could reuse the intent and the startTime would be from much earlier.
        shouldLogLatency = savedInstanceState == null
        startTime = intent.getLongExtra(Intent.EXTRA_START_TIME, INVALID_START_TIME_MS)

        setContent {
            val settingsRepo = SettingsRepo(defaultTheme, getPreferences(MODE_PRIVATE))
            LocalView.current.requestUnbufferedDispatch(InputDevice.SOURCE_TOUCHSCREEN)
            VirtualGamepadTheme {
                CompositionLocalProvider(
                    LocalGamepadState provides gamepadState,
                    LocalDensity provides fixedDensity,
                ) {
                    Gamepad(
                        settingsRepo.getGamepadTheme(),
                        settingsRepo::setGamepadTheme,
                        this::dismissGamepad,
                    )
                }
            }
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    fun isSystemInDarkTheme(): Boolean {
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES
    }

    override fun onStart() {
        super.onStart()
        inputManager.registerInputDeviceListener(this, null)
        gamepadBackend.connect(this)
        logGamepadStart()
    }

    override fun onResume() {
        super.onResume()
        // If the user opts out while the app is running, finish the gamepad activity.
        // Note that the app needs to be restarted if the user opts in.
        if (isGamepadDismissed() || isPhysicalGamepadConnected()) {
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        inputManager.unregisterInputDeviceListener(this)
        gamepadBackend.disconnect()
        logGamepadStop()
    }

    fun dismissGamepad(alwaysHide: Boolean) {
        callerPackageName?.let { pkg ->
            setUserOption(pkg, PackageManager.VIRTUAL_GAMEPAD_USER_OPTION_OPT_OUT)
            if (!alwaysHide) {
                // Hide for now: revert OPT_OUT after 2 seconds.
                // TODO(b/491541162) find a better way to pass the temporary opt-out to the game
                // process.
                Handler(Looper.getMainLooper())
                    .postDelayed(
                        { setUserOption(pkg, PackageManager.VIRTUAL_GAMEPAD_USER_OPTION_UNSET) },
                        2000,
                    )
            }
        }
        finish()
    }

    private fun setUserOption(packageName: String, option: Int) {
        AppGlobals.getPackageManager().setVirtualGamepadUserOption(packageName, userId, option)
    }

    fun isGamepadDismissed(): Boolean =
        callerPackageName != null &&
            AppGlobals.getPackageManager().getVirtualGamepadUserOption(callerPackageName, userId) ==
                PackageManager.VIRTUAL_GAMEPAD_USER_OPTION_OPT_OUT

    override fun onInputDeviceAdded(deviceId: Int) {
        val device = inputManager.getInputDevice(deviceId) ?: return
        if (isPhysicalGamepadDevice(device)) {
            finish()
        }
    }

    override fun onInputDeviceChanged(deviceId: Int) {}

    override fun onInputDeviceRemoved(deviceId: Int) {}

    private fun isPhysicalGamepadConnected(): Boolean {
        for (id in inputManager.inputDeviceIds) {
            val device = inputManager.getInputDevice(id) ?: continue
            if (isPhysicalGamepadDevice(device)) {
                return true
            }
        }
        return false
    }

    private fun isPhysicalGamepadDevice(device: InputDevice): Boolean {
        if (device.isVirtual) return false
        if (device.name == LocalGamepadBackend.name) return false
        val sources = device.sources
        return (sources and InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD ||
            (sources and InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
    }

    private fun logGamepadStart() {
        val latency =
            if (shouldLogLatency && startTime > INVALID_START_TIME_MS) {
                SystemClock.elapsedRealtime() - startTime
            } else {
                INVALID_LATENCY_MS
            }
        shouldLogLatency = false
        VirtualGamepadStatsLog.write(
            VirtualGamepadStatsLog.VIRTUAL_GAMEPAD_STATE_CHANGED,
            VirtualGamepadStatsLog.VIRTUAL_GAMEPAD_STATE_CHANGED__EVENT_TYPE__SESSION_START,
            callerUid,
            VirtualGamepadStatsLog.VIRTUAL_GAMEPAD_STATE_CHANGED__END_REASON__END_REASON_UNKNOWN,
            latency,
        )
    }

    private fun logGamepadStop() {
        VirtualGamepadStatsLog.write(
            VirtualGamepadStatsLog.VIRTUAL_GAMEPAD_STATE_CHANGED,
            VirtualGamepadStatsLog.VIRTUAL_GAMEPAD_STATE_CHANGED__EVENT_TYPE__SESSION_END,
            callerUid,
            VirtualGamepadStatsLog.VIRTUAL_GAMEPAD_STATE_CHANGED__END_REASON__GAME_EXITED,
            INVALID_LATENCY_MS,
        )
    }
}
