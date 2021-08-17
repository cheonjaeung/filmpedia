package io.woong.filmpedia.ui.component

import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.Genres
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

    private var imageClickListener: OnImageClickListener? = null

    fun setOnImageClickListener(listener: OnImageClickListener) {
        imageClickListener = listener
    }

    private var infoButtonClickListener: OnInfoButtonClickListener? = null

    fun setOnInfoButtonClickListener(listener: OnInfoButtonClickListener) {
        infoButtonClickListener = listener
    }

    var movie: RecommendedMovie? = null
        set(value) {
            field = value
            loadMovieInfo()
        }

    init {
        inflate(context, R.layout.layout_recommended_movie_view, this)

        backdropImageView = findViewById(R.id.rmv_backdrop)
        backdropImageView.setOnClickListener(this)

        titleTextView = findViewById(R.id.rmv_title)
        genresTextView = findViewById(R.id.rmv_genres)
        ratingView = findViewById(R.id.rmv_rating)

        infoButton = findViewById(R.id.rmv_info_button)
        infoButton.setOnClickListener(this)

        bookmarkButton = findViewById(R.id.rmv_bookmark_button)
    }

    private fun loadMovieInfo() {
        movie?.let { m ->
            Glide.with(this)
                .load(ImagePathUtil.toFullUrl(m.movie.backdropPath))
                .into(backdropImageView)

            titleTextView.text = m.movie.title

            val g = mutableListOf<Genres.Genre>()
            g.addAll(m.genres)
            try {
                val reason = resources.getString(m.recommendationReason)
                g.add(Genres.Genre(-1, reason))
            } catch (e: Resources.NotFoundException) {}

            genresTextView.genres = g

            ratingView.rating = m.movie.voteAverage
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            backdropImageView.id -> imageClickListener?.onImageClickListener(this@RecommendedMovieView, movie)
            infoButton.id -> infoButtonClickListener?.onInfoButtonClick(this@RecommendedMovieView, movie)
        }
    }

    interface OnImageClickListener {
        fun onImageClickListener(view: RecommendedMovieView, movie: RecommendedMovie?)
    }

    interface OnInfoButtonClickListener {
        fun onInfoButtonClick(view: RecommendedMovieView, movie: RecommendedMovie?)
    }
}
