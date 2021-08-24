package io.woong.filmpedia.data.movie

import com.google.gson.annotations.SerializedName

data class Movie(
    val adult: Boolean,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("belongs_to_collection") val belongsToCollection: Collection?,
    val budget: Int,
    val genres: List<Genres.Genre>,
    val homepage: String?,
    val id: Int,
    @SerializedName("imdb_id") val imdbId: String?,
    @SerializedName("original_language") val originalLanguage: String,
    @SerializedName("original_title") val originalTitle: String,
    val overview: String?,
    val popularity: Double,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("production_companies") val productionCompanies: List<Company>,
    @SerializedName("production_countries") val productionCountries: List<Country>,
    @SerializedName("release_date") val releaseDate: String,
    val revenue: Int,
    val runtime: Int?,
    @SerializedName("spoken_languages") val spokenLanguages: List<Country>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("vote_count") val voteCount: Int
) {

    data class Collection(
        val id: Int,
        val name: String,
        @SerializedName("poster_path") val posterPath: String?,
        @SerializedName("backdrop_path") val backdropPath: String?
    )

    data class Company(
        val name: String,
        val id: Int,
        @SerializedName("logo_path") val logoPath: String?,
        @SerializedName("origin_country") val originCountry: String
    )

    data class Country(
        @SerializedName("iso_3166_1") val iso3166_1: String,
        val name: String,
        @SerializedName("english_name") val englishName: String
    )
}
