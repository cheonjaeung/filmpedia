package io.woong.filmpedia.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpannable
import io.woong.filmpedia.R

class FilmographyTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    var movieTitle: String = ""
    @ColorInt
    private var movieTitleColor: Int = 0
    private var movieTitleSize: Float = 0f

    var department: String = ""
    @ColorInt
    private var departmentColor: Int = 0
    private var departmentSize: Float = 0f

    init {
        applyAttributes(attrs, defStyle)
    }

    private fun applyAttributes(attributes: AttributeSet?, defStyle: Int) {
        val attrs = context.obtainStyledAttributes(
            attributes,
            R.styleable.FilmographyTextView,
            defStyle,
            0
        )

        try {
            val defaultTextColor = currentTextColor
            val defaultTextSize = textSize

            movieTitle = attrs.getString(R.styleable.FilmographyTextView_movie_title) ?: ""
            movieTitleColor = attrs.getColor(R.styleable.FilmographyTextView_movie_title_color, defaultTextColor)
            movieTitleSize = attrs.getDimension(R.styleable.FilmographyTextView_movie_title_size, defaultTextSize)

            department = attrs.getString(R.styleable.FilmographyTextView_department) ?: ""
            departmentColor = attrs.getColor(R.styleable.FilmographyTextView_department_color, defaultTextColor)
            departmentSize = attrs.getDimension(R.styleable.FilmographyTextView_department_size, defaultTextSize)
        } finally {
            attrs.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        text = buildFilmographyText()
    }

    private fun buildFilmographyText(): Spannable {
        val fString = resources.getString(R.string.person_filmography_movie_item, movieTitle, department)

        val builder = SpannableStringBuilder(fString)

        val titleStartPos = 0
        val titleEndPos = movieTitle.length

        val departmentStartPos = titleEndPos + 1
        val departmentEndPos = fString.length

        builder.setSpan(
            ForegroundColorSpan(movieTitleColor),
            titleStartPos,
            titleEndPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        builder.setSpan(
            AbsoluteSizeSpan(movieTitleSize.toInt()),
            titleStartPos,
            titleEndPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        builder.setSpan(
            StyleSpan(Typeface.BOLD),
            titleStartPos,
            titleEndPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        builder.setSpan(
            ForegroundColorSpan(departmentColor),
            departmentStartPos,
            departmentEndPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        builder.setSpan(
            AbsoluteSizeSpan(departmentSize.toInt()),
            departmentStartPos,
            departmentEndPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return builder.toSpannable()
    }
}
