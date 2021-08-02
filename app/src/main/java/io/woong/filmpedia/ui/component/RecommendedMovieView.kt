package io.woong.filmpedia.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import io.woong.filmpedia.R
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.util.ImagePathUtil

class RecommendedMovieView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 1
) : ConstraintLayout(context, attrs, defStyle) {

    private val posterImageView: AppCompatImageView
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
        posterImageView = findViewById(R.id.rmv_poster)
        rateView = findViewById(R.id.rmv_rate)
        infoButton = findViewById(R.id.rmv_info_button)
        favoriteButton = findViewById(R.id.rmv_favorite_button)
    }

    private fun loadMovieInfo() {
        movie?.let { m ->
            m.posterPath?.let { path ->
                Glide.with(this)
                    .load(ImagePathUtil.toFullUrl(path))
                    .into(posterImageView)
            }
        }
    }
}
