package io.woong.filmpedia.data.search

import io.woong.filmpedia.data.movie.Genres

data class SearchResult(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val genres: List<Genres.Genre>,
    val rating: Double
)
