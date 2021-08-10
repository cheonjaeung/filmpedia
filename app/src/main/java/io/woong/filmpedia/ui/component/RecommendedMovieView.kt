package io.woong.filmpedia.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
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
    private val genresTextView: GenresTextView
    private val ratingView: CircularRatingView
    private val infoButton: AppCompatImageButton
    private val bookmarkButton: AppCompatImageButton

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

        bookmarkButton = findViewById(R.id.rmv_bookmark_button)
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

            val g = mutableListOf<Genre>()
            g.addAll(m.genres)
            g.add(Genre(-1, m.recommendationReason))
            genresTextView.genres = g

            ratingView.rating = m.movie.voteAverage
        }
    }

    interface OnInfoButtonClickListener {
        fun onInfoButtonClick(view: RecommendedMovieView, movie: RecommendedMovie?)
    }
}
