package io.woong.filmpedia.data.people

import com.google.gson.annotations.SerializedName

data class Person(
    val adult: Boolean,
    @SerializedName("also_known_as") val alsoKnownAs: List<String>,
    val biography: String,
    val birthday: String?,
    val deathday: String?,
    val gender: Int,
    val homepage: String?,
    val id: Int,
    @SerializedName("imdb_id") val imdbId: String,
    @SerializedName("known_for_department") val knownForDepartment: String,
    val name: String,
    @SerializedName("place_of_birth") val placeOfBirth: String?,
    val popularity: Double,
    @SerializedName("profile_path") val profilePath: String?,
) {

    /**
     * According to TMDB's gender code.
     */
    enum class Gender(val value: Int) {
        UNSPECIFIED(0),
        FEMALE(1),
        MALE(2),
        NON_BINARY(3)
    }
}
