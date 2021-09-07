package io.woong.filmpedia.data.people

import com.google.gson.annotations.SerializedName

data class MovieCredits(
    val id: Int,
    @SerializedName("cast") val casts: List<Cast>,
    @SerializedName("crew") val crews: List<Crew>
) {

    interface SubItem {
        val id: Int
        val title: String
        val releaseDate: String?
    }

    data class Cast(
        override val id: Int,
        override val title: String,
        val character: String,
        @SerializedName("release_date") override val releaseDate: String?
    ) : SubItem

    data class Crew(
        override val id: Int,
        override val title: String,
        val department: String,
        val job: String,
        @SerializedName("release_date") override val releaseDate: String?
    ) : SubItem
}
