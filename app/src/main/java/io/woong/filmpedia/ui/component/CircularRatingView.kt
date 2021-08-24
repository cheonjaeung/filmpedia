package io.woong.filmpedia.ui.component

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import io.woong.filmpedia.R
import io.woong.filmpedia.util.DimensionUtil
import java.lang.IllegalArgumentException
import java.lang.NumberFormatException

class CircularRatingView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    companion object {
        private const val RATING_ARC_START_ANGLE: Float = -90f
    }

    var rating: Double = 0.0
        set(value) {
            field = value
            invalidate()
        }
    private var ratingMax: Double = 0.0
    @ColorInt
    private var ratingTextColor: Int = 0
    private var ratingTextSize: Float = 0f
    private val ratingTextRect: Rect = Rect()
    private val ratingTextPaint: Paint = Paint().apply { isAntiAlias = true }

    @ColorInt
    private var backgroundCircleColor: Int = 0
    private var circleCenterX: Float = 0f
    private var circleCenterY: Float = 0f
    private var backgroundCircleRadius: Float = 0f
    private val backgroundCirclePaint: Paint = Paint().apply { isAntiAlias = true }

    @ColorInt
    private var ratingArcColor: Int = 0
    private var ratingArcSize: Float = 0f
    private val ratingArcRect: RectF = RectF()
    private var ratingArcAngle: Float = 0f
    private val ratingArcPaint: Paint = Paint().apply { isAntiAlias = true }

    @ColorInt
    private var centerCircleColor: Int = 0
    private var centerCircleRadius: Float = 0f
    private val centerCirclePaint: Paint = Paint().apply { isAntiAlias = true }

    init {
        applyAttributes(attrs, defStyle)
    }

    private fun applyAttributes(attributes: AttributeSet?, defStyle: Int) {
        val attrs = context.obtainStyledAttributes(
            attributes,
            R.styleable.CircularRatingView,
            defStyle,
            0
        )

        try {
            val ratingString = attrs.getString(R.styleable.CircularRatingView_rating) ?: "0.0"
            rating = try {
                ratingString.toDouble()
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Attribute 'rating' of CircularRatingView should be a number.")
            }

            val ratingMaxString = attrs.getString(R.styleable.CircularRatingView_rating_max) ?: "10.0"
            ratingMax = try {
                ratingMaxString.toDouble()
            } catch (e: NumberFormatException) {
                throw IllegalArgumentException("Attribute 'rating_max' of CircularRatingView should be a number.")
            }

            ratingArcSize = attrs.getDimension(
                R.styleable.CircularRatingView_rating_arc_size,
                DimensionUtil.dpToPx(4f, context.resources.displayMetrics)
            )

            ratingArcColor = attrs.getColor(
                R.styleable.CircularRatingView_rating_arc_color,
                Color.GREEN
            )

            ratingTextSize = attrs.getDimension(
                R.styleable.CircularRatingView_rating_text_size,
                DimensionUtil.dpToPx(10f, context.resources.displayMetrics)
            )

            ratingTextColor = attrs.getColor(
                R.styleable.CircularRatingView_rating_text_color,
                Color.WHITE
            )

            backgroundCircleColor = attrs.getColor(
                R.styleable.CircularRatingView_background_circle_color,
                Color.LTGRAY
            )

            centerCircleColor = attrs.getColor(
                R.styleable.CircularRatingView_center_circle_color,
                Color.DKGRAY
            )
        } finally {
            attrs.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)

        circleCenterX = (paddingLeft + (width - paddingRight)) / 2f
        circleCenterY = (paddingTop + (height - paddingBottom)) / 2f

        backgroundCircleRadius = if (width < height) {
            (width - paddingLeft - paddingRight) / 2f
        } else {
            (height - paddingTop - paddingBottom) / 2f
        }

        ratingArcRect.set(
            circleCenterX - backgroundCircleRadius,
            circleCenterY - backgroundCircleRadius,
            circleCenterX + backgroundCircleRadius,
            circleCenterY + backgroundCircleRadius
        )

        centerCircleRadius = backgroundCircleRadius - ratingArcSize
    }

    override fun onDraw(canvas: Canvas) {
        val percent = rating.toFloat() / ratingMax.toFloat()

        backgroundCirclePaint.color = backgroundCircleColor

        ratingArcPaint.color = ratingArcColor
        ratingArcAngle = percent * 360.0f

        centerCirclePaint.color = centerCircleColor

        ratingTextPaint.apply {
            color = ratingTextColor
            textSize = ratingTextSize
        }

        measureTextRect(rating.toString())

        canvas.apply {
            drawCircle(
                circleCenterX,
                circleCenterY,
                backgroundCircleRadius,
                backgroundCirclePaint
            )

            drawArc(
                ratingArcRect,
                RATING_ARC_START_ANGLE,
                ratingArcAngle,
                true,
                ratingArcPaint
            )

            drawCircle(
                circleCenterX,
                circleCenterY,
                centerCircleRadius,
                centerCirclePaint
            )

            drawText(
                rating.toString(),
                ratingTextRect.left.toFloat(),
                ratingTextRect.bottom.toFloat(),
                ratingTextPaint
            )
        }
    }

    private fun measureTextRect(text: String) {
        ratingTextPaint.getTextBounds(
            text,
            0,
            text.length,
            ratingTextRect
        )

        val width = ratingTextRect.width()
        val height = ratingTextRect.height()

        val left = (circleCenterX - (width / 2)).toInt()
        val top = (circleCenterY - (height / 2)).toInt()
        val right = (circleCenterX + (width / 2)).toInt()
        val bottom = (circleCenterY + (height / 2)).toInt()

        ratingTextRect.set(left, top, right, bottom)
    }
}
