package com.sbgapps.simplenumberpicker.utils

import android.content.Context
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat

fun Context.color(@ColorRes res: Int) = ContextCompat.getColor(this, res)