package io.woong.filmpedia.data

data class RecommendedMovie(
    val movie: Movies.Movie,
    val genres: List<Genre>,
    val recommendationReason: Int
)
