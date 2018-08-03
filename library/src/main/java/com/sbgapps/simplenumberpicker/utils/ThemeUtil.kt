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
import android.os.Build
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.util.TypedValue

@ColorInt
fun getThemeAccentColor(context: Context): Int {
    val colorAttr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        android.R.attr.colorAccent
    } else {
        context.resources.getIdentifier("colorAccent", "attr", context.packageName)
    }
    val outValue = TypedValue()
    context.theme.resolveAttribute(colorAttr, outValue, true)
    return outValue.data
}

fun makeSelector(context: Context, drawableResId: Int, color: Int): StateListDrawable {
    val stateListDrawable = StateListDrawable()
    stateListDrawable.setExitFadeDuration(50)
    var drawable = ContextCompat.getDrawable(context, drawableResId)
    drawable?.let {
        DrawableCompat.setTint(it, color and 0x40FFFFFF)
        stateListDrawable.addState(intArrayOf(-android.R.attr.state_enabled), drawable)
    }

    drawable = ContextCompat.getDrawable(context, drawableResId)
    drawable?.let {
        DrawableCompat.setTint(it, color)
        stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), drawable)
    }
    return stateListDrawable
}
