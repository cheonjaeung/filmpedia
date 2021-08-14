package io.woong.filmpedia.repository

import io.woong.filmpedia.apiKey
import io.woong.filmpedia.data.*
import io.woong.filmpedia.network.MovieService
import io.woong.filmpedia.network.TmdbClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieRepository {

    private val movieService: MovieService = TmdbClient.instance.create(MovieService::class.java)

    fun fetchMovieDetail(
        movieId: Int,
        onResponse: (movie: Movie?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getDetail(apiKey = apiKey, movieId = movieId)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchCredits(
        movieId: Int,
        onResponse: (credits: Credits?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getCredits(movieId = movieId, apiKey = apiKey)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchExternalIds(
        movieId: Int,
        onResponse: (ids: ExternalIds?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getExternalIds(movieId = movieId, apiKey = apiKey)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchRecommendations(
        movieId: Int,
        onResponse: (movies: Movies?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getRecommendations(movieId = movieId, apiKey = apiKey)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchTop10NowPlayingMovies(
        onResponse: (movies: List<Movies.Movie>) -> Unit
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
        onResponse: (movies: List<Movies.Movie>) -> Unit
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
        onResponse: (movies: List<Movies.Movie>) -> Unit
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
        onResponse: (movies: List<Movies.Movie>) -> Unit
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

    private fun extractTop10Movies(movies: List<Movies.Movie>): List<Movies.Movie> {
        return if (movies.size < 10) {
            movies
        } else {
            val top10 = mutableListOf<Movies.Movie>()
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
