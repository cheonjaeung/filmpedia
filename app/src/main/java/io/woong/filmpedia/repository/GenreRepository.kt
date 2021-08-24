package io.woong.filmpedia.repository

import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.network.GenreService
import io.woong.filmpedia.network.TmdbClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GenreRepository {

    private val genreService: GenreService = TmdbClient.instance.create(GenreService::class.java)

    fun fetchGenres(
        key: String,
        lang: String,
        onResponse: (genres: List<Genres.Genre>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = genreService.getGenres(apiKey = key, language = lang)

        if (response.isSuccessful) {
            val body = response.body()!!
            onResponse(body.genres)
        } else {
            onResponse(emptyList())
        }
    }
}
