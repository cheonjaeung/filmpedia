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

    /**
     * Get the most popular movie on this time for recommending.
     */
    fun fetchRecommendedMovie(
        onResponse: (isSuccess: Boolean, movie: RecommendedMovie?, error: ErrorResponse?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            fetchFirstPagePopularMovies { isSuccess, movie, error ->
                if (isSuccess) {
                    onResponse(true, movie, null)
                } else {
                    onResponse(false, null, error)
                }
            }
        }
    }

    /**
     * Get page 1 of popular movies.
     */
    private fun fetchFirstPagePopularMovies(
        onResponse: (isSuccess: Boolean, movie: RecommendedMovie?, error: ErrorResponse?) -> Unit
    ) {
        TmdbClient.instance
            .create(MovieService::class.java)
            .getPopular(apiKey = apiKey, page = 1)
            .request(
                onSuccess = { result ->
                    val movies = result.body.results
                    if (movies.isNotEmpty()) {
                        val topMovie = movies[0]
                        val topMovieId = topMovie.id
                        fetchTheMostPopularMovie(topMovieId) { isSuccess, movie, error ->
                            if (isSuccess) {
                                onResponse(true, movie, null)
                            } else {
                                onResponse(false, null, error)
                            }
                        }
                    } else {
                        onResponse(false, null, null)
                    }
                },
                onFailure = { result ->
                    onResponse(false, null, result.errorBody)
                },
                onException = { result ->
                    throw result.exception
                }
            )
    }

    /**
     * Get detail information of the most popular movie.
     */
    private fun fetchTheMostPopularMovie(
        movieId: Int,
        onResponse: (isSuccess: Boolean, movie: RecommendedMovie?, error: ErrorResponse?) -> Unit
    ) {
        TmdbClient.instance
            .create(MovieService::class.java)
            .getDetail(movieId = movieId, apiKey = apiKey)
            .request(
                onSuccess = { result ->
                    val rm = RecommendedMovie(
                        movieId = result.body.id,
                        title = result.body.title,
                        posterPath = result.body.posterPath
                    )
                    onResponse(true, rm, null)
                },
                onFailure = { result ->
                    onResponse(false, null, result.errorBody)
                },
                onException = { result ->
                    throw result.exception
                }
            )
    }

    /**
     * Get top 10 now playing movies on this time.
     */
    fun fetchNowPlaying10Movies(
        onResponse: (isSuccess: Boolean, movies: List<Movies.Result>, error: ErrorResponse?) -> Unit
    ) {
        TmdbClient.instance
            .create(MovieService::class.java)
            .getNowPlaying(apiKey = apiKey, page = 1)
            .request(
                onSuccess = { result ->
                    val movies = result.body.results
                    if (movies.isNotEmpty()) {
                        val nowPlayingMovies = mutableListOf<Movies.Result>()
                        if (movies.size >= 10) {
                            nowPlayingMovies.addAll(movies.subList(0, 10))
                        } else {
                            nowPlayingMovies.addAll(movies)
                        }
                        onResponse(true, nowPlayingMovies, null)
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
}
