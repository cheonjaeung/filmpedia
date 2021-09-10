package io.woong.filmpedia.network.service

import io.woong.filmpedia.data.movie.Credits
import io.woong.filmpedia.data.ExternalIds
import io.woong.filmpedia.data.movie.Movie
import io.woong.filmpedia.data.movie.MovieImages
import io.woong.filmpedia.data.movie.Movies
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {

    @GET("movie/{movie_id}")
    suspend fun getDetail(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("append_to_response") appendToResponse: String? = null
    ): Response<Movie>

    @GET("movie/{movie_id}/credits")
    suspend fun getCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null
    ): Response<Credits>

    @GET("movie/{movie_id}/external_ids")
    suspend fun getExternalIds(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String
    ): Response<ExternalIds>

    @GET("movie/{movie_id}/images")
    suspend fun getImages(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null
    ): Response<MovieImages>

    @GET("movie/now_playing")
    fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Call<Movies>

    @GET("movie/popular")
    fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Call<Movies>

    @GET("movie/top_rated")
    fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Call<Movies>
}
