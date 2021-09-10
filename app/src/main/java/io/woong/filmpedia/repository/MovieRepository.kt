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
        onResponse: (result: Result<Movie>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        movieService.getDetail(apiKey = key, movieId = id, language = lang)
            .enqueue(object : Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }

    fun fetchCredits(
        key: String,
        id: Int,
        lang: String,
        onResponse: (result: Result<Credits>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        movieService.getCredits(movieId = id, apiKey = key, language = lang)
            .enqueue(object : Callback<Credits> {
                override fun onResponse(call: Call<Credits>, response: Response<Credits>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Credits>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }

    fun fetchExternalIds(
        key: String,
        id: Int,
        onResponse: (result: Result<ExternalIds>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        movieService.getExternalIds(movieId = id, apiKey = key)
            .enqueue(object : Callback<ExternalIds> {
                override fun onResponse(call: Call<ExternalIds>, response: Response<ExternalIds>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<ExternalIds>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }

    fun fetchImages(
        key: String,
        id: Int,
        onResponse: (result: Result<MovieImages>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        movieService.getImages(movieId = id, apiKey = key)
            .enqueue(object : Callback<MovieImages> {
                override fun onResponse(call: Call<MovieImages>, response: Response<MovieImages>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<MovieImages>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
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
