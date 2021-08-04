package io.woong.filmpedia.data

/**
 * A data class of recommended movie information using in [RecommendedMovieView][io.woong.filmpedia.ui.component.RecommendedMovieView].
 */
data class RecommendedMovie(
    var movieId: Int,
    val title: String,
    val backdropPath: String?,
    val genres: List<Genre>
)
