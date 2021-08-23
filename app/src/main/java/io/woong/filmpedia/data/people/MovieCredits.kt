package io.woong.filmpedia.data.people

import com.google.gson.annotations.SerializedName

data class MovieCredits(
    val id: Int,
    @SerializedName("cast") val casts: List<Cast>,
    @SerializedName("crew") val crews: List<Crew>
) {

    interface CreditsSubItem {
        val id: Int
        val title: String
        val posterPath: String?
    }

    data class Cast(
        override val id: Int,
        override val title: String,
        @SerializedName("poster_path") override val posterPath: String?,
        val character: String
    ) : CreditsSubItem

    data class Crew(
        override val id: Int,
        override val title: String,
        @SerializedName("poster_path") override val posterPath: String?,
        val department: String
    ) : CreditsSubItem
}
