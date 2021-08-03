package io.woong.filmpedia.repository

import io.woong.filmpedia.apiKey
import io.woong.filmpedia.data.ErrorResponse
import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.data.RecommendedMovie
import io.woong.filmpedia.network.MovieService
import io.woong.filmpedia.network.TmdbClient
import io.woong.filmpedia.util.request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeRepository {

    fun fetchRecommendedMovie(
        onResponse: (isSuccess: Boolean, movie: RecommendedMovie?, error: ErrorResponse?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            fetchTop10PopularMovies { isSuccess, movies, error ->
                if (isSuccess) {
                    if (movies.isNotEmpty()) {
                        val topMovie = movies[0]
                        val rm = RecommendedMovie(
                            movieId = topMovie.id,
                            title = topMovie.title,
                            posterPath = topMovie.posterPath
                        )
                        onResponse(true, rm, null)
                    } else {
                        onResponse(false, null, null)
                    }
                } else {
                    onResponse(false, null, error)
                }
            }
        }
    }

    fun fetchTop10NowPlayingMovies(
        onResponse: (isSuccess: Boolean, movies: List<Movies.Result>, error: ErrorResponse?) -> Unit
    ) {
        TmdbClient.instance
            .create(MovieService::class.java)
            .getNowPlaying(apiKey = apiKey, page = 1)
            .request(
                onSuccess = { result ->
                    val movies = result.body.results
                    if (movies.isNotEmpty()) {
                        onResponse(true, movies.top10SubList(), null)
                    } else {
                        onResponse(false, emptyList(), null)
                    }
                },
                onFailure = { result ->
                    onResponse(false, emptyList(), result.errorBody)
                },
                onException = { result ->
                    throw result.exception
                }
            )
    }

    fun fetchTop10PopularMovies(
        onResponse: (isSuccess: Boolean, movies: List<Movies.Result>, error: ErrorResponse?) -> Unit
    ) {
        TmdbClient.instance
            .create(MovieService::class.java)
            .getPopular(apiKey = apiKey, page = 1)
            .request(
                onSuccess = { result ->
                    val movies = result.body.results
                    if (movies.isNotEmpty()) {
                        onResponse(true, movies.top10SubList(), null)
                    } else {
                        onResponse(false, emptyList(), null)
                    }
                },
                onFailure = { result ->
                    onResponse(false, emptyList(), result.errorBody)
                },
                onException = { result ->
                    throw result.exception
                }
            )
    }

    fun fetchTop10RatedMovies(
        onResponse: (isSuccess: Boolean, movie: List<Movies.Result>, error: ErrorResponse?) -> Unit
    ) {
        TmdbClient.instance
            .create(MovieService::class.java)
            .getTopRated(apiKey = apiKey, page = 1)
            .request(
                onSuccess = { result ->
                    val movies = result.body.results
                    if (movies.isNotEmpty()) {
                        onResponse(true, movies.top10SubList(), null)
                    } else {
                        onResponse(false, emptyList(), null)
                    }
                },
                onFailure = { result ->
                    onResponse(false, emptyList(), result.errorBody)
                },
                onException = { result ->
                    throw result.exception
                }
            )
    }

    fun fetchTop10UpcomingMovies(
        onResponse: (isSuccess: Boolean, movie: List<Movies.Result>, error: ErrorResponse?) -> Unit
    ) {
        TmdbClient.instance
            .create(MovieService::class.java)
            .getUpcoming(apiKey = apiKey, page = 1)
            .request(
                onSuccess = { result ->
                    val movies = result.body.results
                    if (movies.isNotEmpty()) {
                        onResponse(true, movies.top10SubList(), null)
                    } else {
                        onResponse(false, emptyList(), null)
                    }},
                onFailure = { result ->
                    onResponse(false, emptyList(), result.errorBody)
                },
                onException = { result ->
                    throw result.exception
                }
            )
    }

    /**
     * Return top 10 movie list.
     * If movie item count is lower than 10, return itself.
     */
    private fun List<Movies.Result>.top10SubList(): List<Movies.Result> {
        return if (this.size < 10) {
            this
        } else {
            val top10 = mutableListOf<Movies.Result>()
            for ((index, movie) in this.withIndex()) {
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
