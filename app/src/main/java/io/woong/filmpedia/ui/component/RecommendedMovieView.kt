package io.woong.filmpedia.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.toSpannable
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.Genre
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.util.ImagePathUtil

class RecommendedMovieView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 1
) : ConstraintLayout(context, attrs, defStyle) {

    private val backdropImageView: AppCompatImageView
    private val titleTextView: AppCompatTextView
    private val genresTextView: AppCompatTextView
    private val rateView: AppCompatButton
    private val infoButton: AppCompatImageButton
    private val favoriteButton: AppCompatImageButton

    var movie: RecommendedMovie? = null
        set(value) {
            field = value
            loadMovieInfo()
        }

    init {
        inflate(context, R.layout.layout_recommended_movie_view, this)
        backdropImageView = findViewById(R.id.rmv_backdrop)
        titleTextView = findViewById(R.id.rmv_title)
        genresTextView = findViewById(R.id.rmv_genres)
        rateView = findViewById(R.id.rmv_rate)
        infoButton = findViewById(R.id.rmv_info_button)
        favoriteButton = findViewById(R.id.rmv_favorite_button)
    }

    private fun loadMovieInfo() {
        movie?.let { m ->
            titleTextView.text = m.title

            genresTextView.text = buildGenresText(m.genres)

            m.backdropPath?.let { path ->
                Glide.with(this)
                    .load(ImagePathUtil.toFullUrl(path))
                    .into(backdropImageView)
            }
        }
    }

    private fun buildGenresText(genres: List<Genre>): Spannable {
        val builder = SpannableStringBuilder()
        genres.forEachIndexed { index, genre ->
            val startPos = builder.length
            builder.append(genre.name)
            val endPos = builder.length

            builder.setSpan(
                ForegroundColorSpan(Color.WHITE),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            if (index < genres.size - 1) {
                builder.append(" · ")
                builder.setSpan(
                    ForegroundColorSpan(Color.YELLOW),
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
