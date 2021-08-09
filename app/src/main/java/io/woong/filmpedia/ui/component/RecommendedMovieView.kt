package io.woong.filmpedia.ui.component

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
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
) : ConstraintLayout(context, attrs, defStyle), View.OnClickListener {

    private val backdropImageView: AppCompatImageView
    private val titleTextView: AppCompatTextView
    private val genresTextView: AppCompatTextView
    private val ratingView: CircularRatingView
    private val infoButton: AppCompatImageButton
    private val favoriteButton: AppCompatImageButton

    private var infoButtonClickListener: OnInfoButtonClickListener? = null

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
        ratingView = findViewById(R.id.rmv_rating)

        infoButton = findViewById(R.id.rmv_info_button)
        infoButton.setOnClickListener(this)

        favoriteButton = findViewById(R.id.rmv_favorite_button)
    }

    override fun onClick(v: View?) {
        if (v?.id == infoButton.id) {
            infoButtonClickListener?.onInfoButtonClick(this@RecommendedMovieView, movie)
        }
    }

    fun setOnInfoButtonClickListener(listener: OnInfoButtonClickListener) {
        infoButtonClickListener = listener
    }

    private fun loadMovieInfo() {
        movie?.let { m ->
            Glide.with(this)
                .load(ImagePathUtil.toFullUrl(m.movie.backdropPath))
                .into(backdropImageView)

            titleTextView.text = m.movie.title

            genresTextView.text = buildGenresText(m.genres)

            ratingView.rating = m.movie.voteAverage
        }
    }

    private fun buildGenresText(genres: List<Genre>): Spannable {
        val builder = SpannableStringBuilder()
        genres.forEach { genre ->
            val startPos = builder.length
            builder.append(genre.name)
            val endPos = builder.length

            builder.setSpan(
                ForegroundColorSpan(Color.WHITE),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

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

        builder.append(movie?.recommendationReason)

        return builder.toSpannable()
    }

    interface OnInfoButtonClickListener {
        fun onInfoButtonClick(view: RecommendedMovieView, movie: RecommendedMovie?)
    }
}
