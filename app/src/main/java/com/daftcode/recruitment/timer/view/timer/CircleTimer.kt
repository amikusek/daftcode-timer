package com.daftcode.recruitment.timer.view.timer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.daftcode.recruitment.timer.view.timer.constants.*
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class CircleTimer : View {

    private var mContext: Context? = null

    private var viewHeight: Int = 0
    private var viewWidth: Int = 0

    private val rect = RectF()
    private var rectLeft: Float = 0F
    private var rectRight: Float = 0F
    private var rectTop: Float = 0F
    private var rectBottom: Float = 0F

    private var centerOfCircleX: Float = 0F
    private var centerOfCircleY: Float = 0F
    private var outerRadius: Float = 0F

    private var newAngle = (ROUND_ANGLE / SEGMENTS_COUNT) * INIT_PERIOD
    private var angle = (ROUND_ANGLE / SEGMENTS_COUNT) * INIT_PERIOD
    private var progress = (ROUND_ANGLE / SEGMENTS_COUNT) * INIT_PERIOD

    val onTimerValueChangeEvents = PublishSubject.create<Long>()
    var isTimerRunning = false

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
    }

    constructor(context: Context) : super(context) {
        mContext = context
    }

    fun setProgress(millis: Long) {
        progress = millis.toFloat() / TimeUnit.SECONDS.toMillis(SEGMENTS_COUNT.toLong()) * ROUND_ANGLE
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        viewHeight = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
        viewWidth = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)

        centerOfCircleX = (viewWidth / 2).toFloat()
        centerOfCircleY = (viewHeight / 2).toFloat()
        outerRadius = (if (viewWidth > viewHeight) viewHeight else viewWidth / 2).toFloat() - CLOCK_DEFAULT_RADIUS

        rectLeft = centerOfCircleX - outerRadius
        rectRight = centerOfCircleX + outerRadius
        rectTop = centerOfCircleY - outerRadius
        rectBottom = centerOfCircleY + outerRadius

        rect.set(rectLeft, rectTop, rectRight, rectBottom)
    }

    override fun onDraw(canvas: Canvas) {
        drawClockFace(canvas)
        drawTimerSelection(canvas)
        drawTimerProgress(canvas)
        drawTimerFaces(canvas)
        super.onDraw(canvas)
    }

    private fun drawClockFace(canvas: Canvas) {
        canvas.drawCircle(centerOfCircleX, centerOfCircleY, outerRadius, Paint().apply {
            color = Color.parseColor(CLOCK_FACE_DEFAULT_COLOR)
            isAntiAlias = false
            strokeWidth = CLOCK_DEFAULT_RADIUS
            style = Paint.Style.STROKE
        })
    }

    private fun drawTimerProgress(canvas: Canvas) {
        canvas.drawArc(rect, START_ANGLE, progress, false, Paint().apply {
            color = Color.parseColor(CLOCK_PROGRESS_DEFAULT_COLOR)
            isAntiAlias = false
            strokeWidth = CLOCK_DEFAULT_RADIUS
            style = Paint.Style.STROKE
        })

        if (!isTimerRunning) {
            canvas.drawArc(rect, progress - 92.5F, THUMB_THICKNESS, false, Paint().apply {
                color = Color.parseColor(THUMB_COLOR)
                isAntiAlias = false
                strokeWidth = CLOCK_DEFAULT_RADIUS + THUMB_WIDTH
                style = Paint.Style.STROKE
            })
        }
    }

    private fun drawTimerSelection(canvas: Canvas) {
        canvas.drawArc(rect, START_ANGLE, angle, false, Paint().apply {
            color = Color.parseColor(SELECTION_DEFAULT_COLOR)
            isAntiAlias = false
            strokeWidth = CLOCK_DEFAULT_RADIUS
            style = Paint.Style.STROKE
        })
    }

    private fun drawTimerFaces(canvas: Canvas) {
        var currentAngle = START_ANGLE
        (1..12).forEach {
            canvas.drawArc(rect, currentAngle - CLOCK_FACE_THICKNESS / 2, CLOCK_FACE_THICKNESS, false, Paint().apply {
                color = if (isTimerRunning) Color.parseColor(DEFAULT_CLOCK_FACES_COLOR_IN_RUNNING_STATE) else Color.parseColor(DEFAULT_CLOCK_FACES_COLOR_IN_STOPPED_STATE)
                isAntiAlias = false
                strokeWidth = CLOCK_DEFAULT_RADIUS - CLOCK_FACE_MARGIN
                style = Paint.Style.STROKE
            })
            currentAngle += ROUND_ANGLE / SEGMENTS_COUNT
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isTimerRunning) {
            val x = event.x
            val y = event.y
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    onMoveEvents(x, y)
                }
                MotionEvent.ACTION_MOVE -> {
                    onMoveEvents(x, y)
                }
                MotionEvent.ACTION_UP -> {
                    onMoveEvents(x, y)
                }
            }
        }
        return true
    }

    private fun onMoveEvents(x: Float, y: Float) {
        newAngle = Math.round(((Math.toDegrees(Math.atan2((x - centerOfCircleY).toDouble(),
                (centerOfCircleY - y).toDouble())) + ROUND_ANGLE).toFloat() % ROUND_ANGLE)).toFloat()

        if (checkIfDiffBetweenToAnglesIsEqualToSegment(angle, newAngle) &&
                checkIfDiffBetweenTwoAnglesIsAtLeastOneSegmentLong(angle, newAngle) &&
                checkIfProgressReachedTopLimit(angle, newAngle) &&
                checkIfProgressIsGreaterThanBottomLimit(newAngle)) {

            angle = roundAngleValueToSegment(newAngle)
            progress = angle
            onTimerValueChangeEvents.onNext(convertAngleToMillis(angle))
            invalidate()
        }
    }

    private fun checkIfDiffBetweenToAnglesIsEqualToSegment(angle: Float, newAngle: Float) =
            Math.abs(roundAngleValueToSegment(angle) - roundAngleValueToSegment(newAngle)) == CLOCK_SEGMENT_ANGLE

    private fun checkIfDiffBetweenTwoAnglesIsAtLeastOneSegmentLong(angle: Float, newAngle: Float) =
            Math.abs(angle - newAngle) >= CLOCK_SEGMENT_ANGLE - 3F

    private fun checkIfProgressReachedTopLimit(angle: Float, newAngle: Float) =
            Math.abs(angle - newAngle).toInt() <= ROUND_ANGLE / 2

    private fun checkIfProgressIsGreaterThanBottomLimit(angle: Float) =
            angle >= 29F

    private fun convertAngleToMillis(angle: Float) =
            (angle / CLOCK_SEGMENT_ANGLE).toLong() * TimeUnit.SECONDS.toMillis(1)

    private fun roundAngleValueToSegment(angle: Float) =
            (Math.ceil((angle / CLOCK_SEGMENT_ANGLE).toDouble()) * CLOCK_SEGMENT_ANGLE).toFloat()
}
