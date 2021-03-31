package com.raywenderlich.emotionalface

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View

class EmotionalFaceView(
        context: Context,
        attrs: AttributeSet
) : View(context, attrs) {

    companion object {
        // Colors
        private const val DEFAULT_FACE_COLOR = Color.YELLOW
        private const val DEFAULT_EYES_COLOR = Color.BLACK
        private const val DEFAULT_MOUTH_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_COLOR = Color.BLACK

        // Dimens
        private const val DEFAULT_BORDER_WIDTH = 4.0F

        // States
        const val HAPPY = 0
        const val SAD = 1
    }

    // Colors
    private var faceColor = DEFAULT_FACE_COLOR
    private var eyesColor = DEFAULT_EYES_COLOR
    private var mouthColor = DEFAULT_MOUTH_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR

    // Dimens
    private var borderWidth = DEFAULT_BORDER_WIDTH

    // States
    var state = HAPPY
        set(value) {
            field = value
            invalidate()
        }

    // Other
    private val paint = Paint()
    private val mouthPath = Path()
    private var size = 0

    // ------------------------------------------------------------| Initializer |
    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
    }

    // ------------------------------------------------------------| Setup Attributes |
    private fun setupAttributes(attrs: AttributeSet) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.EmotionalFaceView, 0, 0)

        typedArray.apply {
            // Colors
            faceColor = getColor(R.styleable.EmotionalFaceView_faceColor, DEFAULT_FACE_COLOR)
            eyesColor = getColor(R.styleable.EmotionalFaceView_eyesColor, DEFAULT_EYES_COLOR)
            mouthColor = getColor(R.styleable.EmotionalFaceView_mouthColor, DEFAULT_MOUTH_COLOR)
            borderColor = getColor(R.styleable.EmotionalFaceView_borderColor, DEFAULT_BORDER_COLOR)
            // Dimens
            borderWidth = getDimension(R.styleable.EmotionalFaceView_borderWidth, DEFAULT_BORDER_WIDTH)
            // States
            state = getInt(R.styleable.EmotionalFaceView_state, HAPPY)
        }.recycle()
    }

    // ------------------------------------------------------------| Draw functions |
    private fun drawFaceBackground(canvas: Canvas) {
        val radius = size / 2F

        paint.color = faceColor
        paint.style = Paint.Style.FILL

        canvas.drawCircle(size / 2F, size / 2F, radius, paint)

        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth

        canvas.drawCircle(size / 2F, size / 2F, radius - borderWidth / 2F, paint)
    }

    private fun drawEyes(canvas: Canvas) {
        paint.color = eyesColor
        paint.style = Paint.Style.FILL

        val leftEyeRect = RectF(size * 0.2F, size * 0.2F, size * 0.35F, size * 0.5F)

        canvas.drawOval(leftEyeRect, paint)

        val rightEyeRect = RectF(size * 0.8F, size * 0.2F, size * 0.65F, size * 0.5F)

        canvas.drawOval(rightEyeRect, paint)
    }

    private fun drawMouth(canvas: Canvas) {
        mouthPath.reset()

        mouthPath.moveTo(size * 0.2F, size * 0.7F)

        if (state == HAPPY) {
            mouthPath.quadTo(size * 0.5F, size * 0.8F, size * 0.8F, size * 0.7F)
            mouthPath.quadTo(size * 0.5F, size * 0.9F, size * 0.2F, size * 0.7F)
        } else {
            mouthPath.quadTo(size * 0.5F, size * 0.6F, size * 0.8F, size * 0.7F)
            mouthPath.quadTo(size * 0.5F, size * 0.5F, size * 0.2F, size * 0.7F)
        }

        paint.color = mouthColor
        paint.style = Paint.Style.FILL

        canvas.drawPath(mouthPath, paint)
    }

    // ------------------------------------------------------------| Overrides |
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        drawFaceBackground(canvas)
        drawEyes(canvas)
        drawMouth(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        size = measuredWidth.coerceAtMost(measuredHeight)
        setMeasuredDimension(size, size)
    }

    // ------------------------------------------------------------| SaveInstanceState |
    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putInt("state", state)
        bundle.putParcelable("superState", super.onSaveInstanceState())
        return bundle
    }

    override fun onRestoreInstanceState(parcel: Parcelable?) {
        var viewState = parcel
        if (viewState is Bundle){
            state = viewState.getInt("state", HAPPY)
            viewState = viewState.getParcelable("superState")
        }
        super.onRestoreInstanceState(viewState)
    }
}