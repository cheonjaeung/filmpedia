package io.woong.filmpedia.repository

import io.woong.filmpedia.apiKey
import io.woong.filmpedia.data.*
import io.woong.filmpedia.network.GenreService
import io.woong.filmpedia.network.MovieService
import io.woong.filmpedia.network.TmdbClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeRepository {

    private val movieService: MovieService = TmdbClient.instance.create(MovieService::class.java)
    private val genreService: GenreService = TmdbClient.instance.create(GenreService::class.java)

    fun fetchGenres(
        onResponse: (genres: List<Genre>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = genreService.getGenres(apiKey = apiKey)

        if (response.isSuccessful) {
            val body = response.body()!!
            onResponse(body.genres)
        } else {
            onResponse(emptyList())
        }
    }

    fun fetchTop10NowPlayingMovies(
        onResponse: (movies: List<Movies.Result>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getNowPlaying(apiKey = apiKey, page = 1)

        if (response.isSuccessful) {
            val movies: Movies = response.body()!!
            val top10 = extractTop10Movies(movies.results)
            onResponse(top10)
        } else {
            onResponse(emptyList())
        }
    }

    fun fetchTop10PopularMovies(
        onResponse: (movies: List<Movies.Result>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getPopular(apiKey = apiKey, page = 1)

        if (response.isSuccessful) {
            val movies: Movies = response.body()!!
            val top10 = extractTop10Movies(movies.results)
            onResponse(top10)
        } else {
            onResponse(emptyList())
        }
    }

    fun fetchTop10HighRateMovies(
        onResponse: (movies: List<Movies.Result>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getTopRated(apiKey = apiKey, page = 1)

        if (response.isSuccessful) {
            val movies: Movies = response.body()!!
            val top10 = extractTop10Movies(movies.results)
            onResponse(top10)
        } else {
            onResponse(emptyList())
        }
    }

    fun fetchTop10UpcomingMovies(
        onResponse: (movies: List<Movies.Result>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getUpcoming(apiKey = apiKey, page = 1)

        if (response.isSuccessful) {
            val movies: Movies = response.body()!!
            val top10 = extractTop10Movies(movies.results)
            onResponse(top10)
        } else {
            onResponse(emptyList())
        }
    }

    private fun extractTop10Movies(movies: List<Movies.Result>): List<Movies.Result> {
        return if (movies.size < 10) {
            movies
        } else {
            val top10 = mutableListOf<Movies.Result>()
            for ((index, movie) in movies.withIndex()) {
                if (index < 10) {
                    top10.add(movie)
                } else {
                    break
                }
            }
            top10
        }
    }
}
