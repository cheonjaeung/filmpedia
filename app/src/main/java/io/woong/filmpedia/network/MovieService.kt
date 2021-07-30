package io.woong.filmpedia.network

import io.woong.filmpedia.data.Movie
import io.woong.filmpedia.data.Movies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit service interface of TMDB movie API.
 */
interface MovieService {

    /**
     * Get the primary information about a movie.
     */
    @GET("movie/{movie_id}")
    fun getDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("append_to_response") appendToResponse: String? = null
    ): Call<Movie>

    /**
     * Get a list of the current popular movies on TMDB.
     */
    @GET("movie/popular")
    fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Call<Movies>
}
