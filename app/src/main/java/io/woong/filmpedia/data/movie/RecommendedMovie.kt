package io.woong.filmpedia.data.movie

data class RecommendedMovie(
    val movie: Movies.Movie,
    val genres: List<Genres.Genre>,
    val recommendationReason: Int
)
