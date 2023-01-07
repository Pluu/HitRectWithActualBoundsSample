package com.pluu.utils

import android.content.Context

fun Context.dp2px(dpValue: Int): Int {
    return dp2pxf(dpValue).toInt()
}

fun Context.dp2pxf(dpValue: Int): Float {
    val scale = resources.displayMetrics.density
    return (dpValue * scale + 0.5f)
}