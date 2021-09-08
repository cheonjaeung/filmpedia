package io.woong.filmpedia.data.people

import io.woong.filmpedia.ui.page.person.FilmographyAdapter

sealed class Filmography(val viewType: FilmographyAdapter.ViewType) {

    data class Profile(
        val name: String,
        val profilePath: String?,
        val birthday: String?,
        val deathday: String?,
        val gender: Person.Gender,
        val placeOfBirth: String?
    ) : Filmography(FilmographyAdapter.ViewType.PROFILE)

    data class Biography(
        val content: String?
    ) : Filmography(FilmographyAdapter.ViewType.BIOGRAPHY)

    data class Title(
        val text: String
    ) : Filmography(FilmographyAdapter.ViewType.TITLE)

    data class Movie(
        val id: Int,
        val title: String,
        val releaseYear: String,
        val department: String
    ) : Filmography(FilmographyAdapter.ViewType.MOVIE)

    class Divider : Filmography(FilmographyAdapter.ViewType.DIVIDER)
}
