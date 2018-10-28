package com.sbgapps.simplenumberpicker.utils

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun Context.color(@ColorRes res: Int) = ContextCompat.getColor(this, res)