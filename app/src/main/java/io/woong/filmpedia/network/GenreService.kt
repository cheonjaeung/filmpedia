package io.woong.filmpedia.network

import io.woong.filmpedia.data.movie.Genres
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface of TMDB movie genres.
 */
interface GenreService {

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null
    ): Response<Genres>
}