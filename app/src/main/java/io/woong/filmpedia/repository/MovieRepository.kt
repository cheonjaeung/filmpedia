package io.woong.filmpedia.repository

import io.woong.filmpedia.data.*
import io.woong.filmpedia.data.movie.Credits
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.movie.MovieImages
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.network.MovieService
import io.woong.filmpedia.network.TmdbClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieRepository {

    private val movieService: MovieService = TmdbClient.instance.create(MovieService::class.java)

    fun fetchMovieDetail(
        key: String,
        id: Int,
        lang: String,
        onResponse: (movie: Movie?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getDetail(apiKey = key, movieId = id, language = lang)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchCredits(
        key: String,
        id: Int,
        lang: String,
        onResponse: (credits: Credits?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getCredits(movieId = id, apiKey = key, language = lang)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchExternalIds(
        key: String,
        id: Int,
        onResponse: (ids: ExternalIds?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getExternalIds(movieId = id, apiKey = key)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchImages(
        key: String,
        id: Int,
        onResponse: (images: MovieImages?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getImages(movieId = id, apiKey = key)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchRecommendations(
        key: String,
        id: Int,
        lang: String,
        onResponse: (movies: Movies?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getRecommendations(movieId = id, apiKey = key, language = lang)

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }

    fun fetchNowPlayingMovies(
        key: String,
        page: Int,
        lang: String,
        region: String,
        onResponse: (movies: Movies?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getNowPlaying(apiKey = key, page = page, language = lang, region = region)

        if (response.isSuccessful) {
            onResponse(response.body()!!)
        } else {
            onResponse(null)
        }
    }

    fun fetchPopularMovies(
        key: String,
        page: Int,
        lang: String,
        region: String,
        onResponse: (movies: Movies?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getPopular(apiKey = key, page = page, language = lang, region = region)

        if (response.isSuccessful) {
            onResponse(response.body()!!)
        } else {
            onResponse(null)
        }
    }

    fun fetchHighRatedMovies(
        key: String,
        page: Int,
        lang: String,
        region: String,
        onResponse: (movies: Movies?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = movieService.getTopRated(apiKey = key, page = page, language = lang, region = region)

        if (response.isSuccessful) {
            onResponse(response.body()!!)
        } else {
            onResponse(null)
        }
    }
}
