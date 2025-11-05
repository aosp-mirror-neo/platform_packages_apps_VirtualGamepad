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

package com.android.virtualgamepad.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset

class LayoutEditor() : Parcelable {
    val sharedPreferenceKey = "LayoutEditor"

    var moving by mutableStateOf(false)
    var resizing by mutableStateOf(false)

    val offsetPx = mutableStateMapOf<String, Offset>().withDefault { Offset(0f, 0f) }
    val scale = mutableStateMapOf<String, Float>().withDefault { 1f }

    fun reset() {
        offsetPx.clear()
        scale.clear()
    }

    // TODO we shouldn't use parcel to store persistent data. This is just a temporary solution.
    // Use serialization instead.
    fun save(sharedPref: SharedPreferences?) {
        sharedPref ?: return
        with(sharedPref.edit()) {
            val parcel = Parcel.obtain()
            this@LayoutEditor.writeToParcel(parcel, 0)
            putString(sharedPreferenceKey, parcel.marshall().toHexString())
            apply()

            parcel.recycle()
        }
    }

    fun load(sharedPref: SharedPreferences?) {
        sharedPref ?: return
        sharedPref.getString(sharedPreferenceKey, null)?.also {
            val bytes = it.hexToByteArray()
            val parcel = Parcel.obtain()
            parcel.unmarshall(bytes, 0, bytes.size)
            parcel.setDataPosition(0)

            readFromParcel(parcel)

            parcel.recycle()
        }
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeBoolean(moving)
        dest.writeBoolean(resizing)
        dest.writeBundle(
            Bundle().also {
                offsetPx.forEach { (key, value) ->
                    it.putFloatArray(key, arrayOf(value.x, value.y).toFloatArray())
                }
            }
        )
        dest.writeBundle(
            Bundle().also { scale.forEach { (key, value) -> it.putFloat(key, value) } }
        )
    }

    private constructor(parcel: Parcel) : this() {
        readFromParcel(parcel)
    }

    fun readFromParcel(parcel: Parcel) {
        moving = parcel.readBoolean()
        resizing = parcel.readBoolean()
        parcel.readBundle()!!.also { bundle ->
            bundle.keySet().forEach { key ->
                bundle.getFloatArray(key)?.also { offsetPx[key] = Offset(it[0], it[1]) }
            }
        }
        parcel.readBundle()!!.also { bundle ->
            bundle.keySet().forEach { key -> scale[key] = bundle.getFloat(key) }
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LayoutEditor?> {
        override fun createFromParcel(`in`: Parcel): LayoutEditor {
            return LayoutEditor(`in`)
        }

        override fun newArray(size: Int): Array<LayoutEditor?> {
            return arrayOfNulls(size)
        }
    }
}
