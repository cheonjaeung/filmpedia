package io.woong.filmpedia.data

import com.google.gson.annotations.SerializedName

data class Credits(
    val id: Int,
    val cast: List<Cast>,
    val crew: List<Crew>
) {

    data class Cast(
        val adult: Boolean,
        val gender: Int?,
        val id: Int,
        @SerializedName("known_for_department") val knownForDepartment: String,
        val name: String,
        @SerializedName("original_name") val originalName: String,
        val popularity: Double,
        @SerializedName("profile_path") val profilePath: String?,
        @SerializedName("cast_id") val castId: Int,
        val character: String,
        @SerializedName("credit_id") val creditId: String,
        val order: Int
    )

    data class Crew(
        val adult: Boolean,
        val gender: Int?,
        val id: Int,
        @SerializedName("known_for_department") val knownForDepartment: String,
        val name: String,
        @SerializedName("original_name") val originalName: String,
        val popularity: Double,
        @SerializedName("profile_path") val profilePath: String?,
        @SerializedName("cast_id") val castId: Int,
        val department: String,
        val job: String
    )
}
