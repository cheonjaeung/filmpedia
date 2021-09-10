package io.woong.filmpedia.repository

import io.woong.filmpedia.data.*
import io.woong.filmpedia.data.movie.Credits
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.movie.MovieImages
import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.network.service.MovieService
import io.woong.filmpedia.network.TmdbClient
import io.woong.filmpedia.network.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

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

    fun fetchPopularMovies(
        key: String,
        page: Int,
        lang: String,
        region: String,
        onResponse: (result: Result<Movies>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        movieService.getPopular(apiKey = key, page = page, language = lang, region = region)
            .enqueue(object : Callback<Movies> {
                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Movies>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }

    fun fetchNowPlayingMovies(
        key: String,
        page: Int,
        lang: String,
        region: String,
        onResponse: (result: Result<Movies>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        movieService.getNowPlaying(apiKey = key, page = page, language = lang, region = region)
            .enqueue(object : Callback<Movies> {
                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Movies>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }

    fun fetchHighRatedMovies(
        key: String,
        page: Int,
        lang: String,
        region: String,
        onResponse: (result: Result<Movies>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        movieService.getPopular(apiKey = key, page = page, language = lang, region = region)
            .enqueue(object : Callback<Movies> {
                override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Movies>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }
}
