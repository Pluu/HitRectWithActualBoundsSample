package com.pluu.hitrect.actualbounds

import android.view.MotionEvent

fun MotionEvent.actionName(): String {
    return when (actionMasked) {
        MotionEvent.ACTION_DOWN -> "ACTION_DOWN"
        MotionEvent.ACTION_MOVE -> "ACTION_MOVE"
        MotionEvent.ACTION_UP -> "ACTION_UP"
        MotionEvent.ACTION_CANCEL -> "ACTION_CANCEL"
        else -> "Unknown"
    }
}