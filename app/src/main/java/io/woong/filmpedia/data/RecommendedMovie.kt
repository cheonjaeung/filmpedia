package io.woong.filmpedia.data

/**
 * A data class of recommended movie information using in [RecommendedMovieView][io.woong.filmpedia.ui.component.RecommendedMovieView].
 */
data class RecommendedMovie(
    val movie: Movies.Result,
    val genres: List<Genre>,
    val recommendationReason: String
)
