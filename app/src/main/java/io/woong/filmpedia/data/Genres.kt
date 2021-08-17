package io.woong.filmpedia.data

data class Genres(
    val genres: List<Genre>
) {

    data class Genre(
        val id: Int,
        val name: String
    )
}
