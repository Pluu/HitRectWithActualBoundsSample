package com.pluu.hitrect.actualbounds

import android.graphics.Rect
import android.view.MotionEvent
import android.view.TouchDelegate
import android.view.View
import logcat.logcat

class ActualBoundsTouchDelegate(
    private val targetBounds: Rect,
    private val actualBounds: Rect,
    private val delegateView: View,
    private val isBoundDebug: Boolean
) : TouchDelegate(targetBounds, delegateView) {

    private var isDelegateTargeted = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        var sendToDelegate = false
        var hit = true
        var handled = false

        if (isBoundDebug) {
            logcat { "[delegate] action:${event.actionName()} = x:${event.x}, y:${event.y}" }
            logcat { "[delegate] target:${targetBounds}" }
            logcat { "[delegate] actualBounds:${actualBounds}" }
        }

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                isDelegateTargeted = targetBounds.contains(x, y)
                sendToDelegate = isDelegateTargeted
            }
            MotionEvent.ACTION_POINTER_DOWN,
            MotionEvent.ACTION_POINTER_UP,
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_MOVE -> {
                sendToDelegate = isDelegateTargeted
                if (sendToDelegate) {
                    if (!targetBounds.contains(x, y)) {
                        hit = false
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                sendToDelegate = isDelegateTargeted
                isDelegateTargeted = false
            }
        }
        if (sendToDelegate) {
            if (hit && !actualBounds.contains(x, y)) {
                // Offset event coordinates to be inside the target view
                event.setLocation(
                    if (x < actualBounds.left) 0f
                    else if (actualBounds.right < x) actualBounds.width().toFloat()
                    else (x - actualBounds.left).toFloat(),
                    if (y < actualBounds.top) 0f
                    else if (actualBounds.bottom < y) actualBounds.height().toFloat()
                    else (y - actualBounds.top).toFloat()
                )
            } else {
                event.setLocation(
                    (x - actualBounds.left).toFloat(),
                    (y - actualBounds.top).toFloat()
                )
            }
            handled = delegateView.dispatchTouchEvent(event)
        }
        return handled
    }
}