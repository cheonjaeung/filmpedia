package io.woong.filmpedia.data.movie

import com.google.gson.annotations.SerializedName

data class Movies(
    val page: Int,
    val results: List<Movie>,
    val dates: Dates?,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
) {

    data class Movie(
        val adult: Boolean,
        @SerializedName("backdrop_path") val backdropPath: String,
        @SerializedName("genre_ids") val genreIds: List<Int>,
        val id: Int,
        @SerializedName("original_language") val originalLanguage: String,
        @SerializedName("original_title") val originalTitle: String,
        val overview: String,
        @SerializedName("release_date") val releaseDate: String,
        @SerializedName("poster_path") val posterPath: String?,
        val title: String,
        val video: Boolean,
        val popularity: Double,
        @SerializedName("vote_average") val voteAverage: Double,
        @SerializedName("vote_count") val voteCount: Int
    )

    data class Dates(
        val maximum: String,
        val minimum: String
    )
}
