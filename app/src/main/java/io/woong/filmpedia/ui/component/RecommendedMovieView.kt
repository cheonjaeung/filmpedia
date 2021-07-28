package io.woong.filmpedia.ui.component

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import io.woong.filmpedia.R
import java.util.*

class RecommendedMovieView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 1
) : ConstraintLayout(context, attrs, defStyle) {

    var movieId: String = ""
        set(value) {
            field = value
            loadMovieInfo()
        }

    init {
        inflate(context, R.layout.layout_recommended_movie_view, this)
        applyAttributes(attrs, defStyle)
    }

    private fun applyAttributes(attributes: AttributeSet?, defStyle: Int) {
        val attrs = context.obtainStyledAttributes(
            attributes,
            R.styleable.RecommendedMovieView,
            defStyle,
            0
        )

        try {
            movieId = attrs.getString(R.styleable.RecommendedMovieView_movie_id) ?: ""
        } finally {
            attrs.recycle()
        }
    }

    private fun loadMovieInfo() {
        if (isValidMovieId(movieId)) {

        }
    }

    private fun isValidMovieId(id: String?): Boolean {
        return when {
            id == null -> false
            id.isEmpty() -> false
            id.isBlank() -> false
            id.lowercase(Locale.getDefault()) == "null" -> false
            else -> true
        }
    }
}
