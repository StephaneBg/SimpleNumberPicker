/*
 * Copyright 2017 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sbgapps.simplenumberpicker.utils

import android.content.Context
import android.graphics.drawable.StateListDrawable
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

fun makeSelector(context: Context, drawableResId: Int, color: Int): StateListDrawable {
    val stateListDrawable = StateListDrawable()
    stateListDrawable.setExitFadeDuration(50)
    VectorDrawableCompat.create(context.resources, drawableResId, null)?.let {
        DrawableCompat.setTint(it, color and 0x40FFFFFF)
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), it)
    }

    VectorDrawableCompat.create(context.resources, drawableResId, null)?.let {
        DrawableCompat.setTint(it, color)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), it)
    }
    return stateListDrawable
}
