package io.woong.filmpedia.data.movie

import com.google.gson.annotations.SerializedName

data class MovieImages(
    val id: Int,
    val backdrops: List<MovieImage>,
    val posters: List<MovieImage>
) {

    data class MovieImage(
        @SerializedName("file_path") val path: String,
        val width: Int,
        val height: Int,
        @SerializedName("aspect_ratio") val aspectRatio: Double,
        @SerializedName("iso_639_1") val iso639_1: String?
    )
}
