package com.pluu.hitrect.actualbounds.sample

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.doOnAttach
import androidx.core.view.doOnPreDraw
import com.pluu.hitrect.actualbounds.ActualBoundsTouchDelegate
import com.pluu.hitrect.actualbounds.databinding.WidgetSampleBinding
import com.pluu.utils.dp2px
import logcat.logcat

class SampleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: WidgetSampleBinding = WidgetSampleBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    private val currentViewBounds = Rect()
    private val expandedBounds = Rect()
    private val debugExpandedBounds = Rect()

    // Debug Mode
    private val point = PointF(0f, 0f)
    private lateinit var debugPaint: Paint
    private lateinit var draggablePaint: Paint

    init {
        setUpExpandTouch()
        setUpDebugMode()
    }

    private fun setUpExpandTouch() {
        doOnAttach {
            val parentView = parent as ViewGroup
            parentView.doOnPreDraw { p ->
                this.getHitRect(currentViewBounds)

                // Sample expand area
                val expandSize = p.context.dp2px(30)

                // View 터치 가능한 영역 확장
                expandedBounds.set(
                    currentViewBounds.left - expandSize,
                    currentViewBounds.top - expandSize,
                    currentViewBounds.right + expandSize,
                    currentViewBounds.bottom + expandSize
                )

                debugExpandedBounds.set(expandedBounds)
                debugExpandedBounds.offsetTo(-expandSize , -expandSize )

                logcat { "currentViewBounds:${currentViewBounds}" }
                logcat { "expandedBounds:${expandedBounds}" }
                logcat { "debugExpandedBounds:${debugExpandedBounds}" }

                parentView.touchDelegate = ActualBoundsTouchDelegate(
                    targetBounds = expandedBounds,
                    actualBounds = currentViewBounds,
                    this,
                    false
                )
            }
        }
    }

    private fun setUpDebugMode() {
        setWillNotDraw(false)

        debugPaint = Paint().apply {
            color = Color.RED
            alpha = 63
        }
        draggablePaint = Paint().apply {
            color = Color.GREEN
            alpha = 63
        }

        doOnAttach {
            val p = parent as ViewGroup
            p.clipChildren = false

            background = createBoundaryDrawable()
            backgroundTintList = ColorStateList.valueOf(0xFF7E57C2.toInt())
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
//        logcat { "action:${event.actionName()} = x:${event.x}, y:${event.y}" }
//        logcat { "action:${event.actionName()} = rawX:${event.rawX}, rawY:${event.rawY}" }

        point.x = event.x
        point.y = event.y
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(point.x, point.y, 15f, debugPaint)
        canvas.drawRect(debugExpandedBounds, debugPaint)
    }

    private fun createBoundaryDrawable(): Drawable {
        val drawable = GradientDrawable()
        drawable.setStroke(context.dp2px(2), Color.RED)
        drawable.color = ColorStateList.valueOf(Color.TRANSPARENT)
        return drawable
    }
}