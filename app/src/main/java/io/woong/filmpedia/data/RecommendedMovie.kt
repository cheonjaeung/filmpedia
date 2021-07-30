package io.woong.filmpedia.data

data class RecommendedMovie(
    var movieId: Int,
    val title: String,
    val posterPath: String?,
)
