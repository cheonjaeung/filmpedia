package io.woong.filmpedia.network

import io.woong.filmpedia.data.Credits
import io.woong.filmpedia.data.ExternalIds
import io.woong.filmpedia.data.Movie
import io.woong.filmpedia.data.Movies
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

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Response<Movies>

    @GET("movie/popular")
    suspend fun getPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Response<Movies>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Response<Movies>

    @GET("movie/upcoming")
    suspend fun getUpcoming(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("page") page: Int? = null,
        @Query("region") region: String? = null
    ): Response<Movies>
}
