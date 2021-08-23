package io.woong.filmpedia.data

import com.google.gson.annotations.SerializedName

data class Person(
    val birthday: String?,
    @SerializedName("known_for_department") val knownForDepartment: String,
    val deathDay: String?,
    val id: Int,
    val name: String,
    @SerializedName("also_known_as") val alsoKnownAs: List<String>,
    val gender: Int,
    val biography: String,
    val popularity: Double,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    @SerializedName("profile_path") val profilePath: String?,
    val adult: Boolean,
    @SerializedName("imdb_id") val imdbId: String,
    val homepage: String?
)
