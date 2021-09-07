package io.woong.filmpedia.data.people

data class Filmography(
    val id: Int,
    val viewType: Int,
    val releasedYear: String,
    val movieTitle: String,
    val department: String
)
