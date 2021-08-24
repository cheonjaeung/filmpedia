package io.woong.filmpedia.ui.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.toSpannable
import io.woong.filmpedia.R
import io.woong.filmpedia.data.movie.Genres

class GenresTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatTextView(context, attrs, defStyle) {

    companion object {
        const val DEFAULT_SEPARATOR: String = "·"
    }

    private val _genres: MutableList<Genres.Genre> = mutableListOf()
    var genres: List<Genres.Genre>
        get() = _genres
        set(value) {
            _genres.apply {
                clear()
                addAll(value)
            }
        }

    private var separator: String = ""
        set(value) {
            field = value
            text = buildGenresText()
        }

    @ColorInt
    private var separatorColor: Int = Color.WHITE
        set(value) {
            field = value
            text = buildGenresText()
        }

    init {
        applyAttributes(attrs, defStyle)
    }

    private fun applyAttributes(attributes: AttributeSet?, defStyle: Int) {
        val attrs = context.obtainStyledAttributes(
            attributes,
            R.styleable.GenresTextView,
            defStyle,
            0
        )

        try {
            val genresString = attrs.getString(R.styleable.GenresTextView_genres) ?: ""
            _genres.apply {
                clear()
                addAll(parseGenresAttribute(genresString))
            }

            separator = attrs.getString(R.styleable.GenresTextView_separator) ?: DEFAULT_SEPARATOR

            separatorColor = attrs.getColor(
                R.styleable.GenresTextView_separator_color,
                currentTextColor
            )
        } finally {
            attrs.recycle()
        }
    }

    private fun parseGenresAttribute(attr: String): List<Genres.Genre> {
        val list = mutableListOf<Genres.Genre>()

        val substrings = attr.split(",")
        substrings.forEach { substring ->
            val s = substring.trim()
            list.add(Genres.Genre(-1, s))
        }

        return list
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        text = buildGenresText()
    }

    private fun buildGenresText(): Spannable {
        val builder = SpannableStringBuilder()
        _genres.forEachIndexed { index, genre ->
            val startPos = builder.length
            builder.append(genre.name)
            val endPos = builder.length

            builder.setSpan(
                ForegroundColorSpan(currentTextColor),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (index < _genres.lastIndex) {
                builder.append(" $separator ")
                builder.setSpan(
                    ForegroundColorSpan(separatorColor),
                    builder.length - 2,
                    builder.length - 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.setSpan(
                    StyleSpan(Typeface.BOLD),
                    builder.length - 2,
                    builder.length - 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return builder.toSpannable()
    }
}
