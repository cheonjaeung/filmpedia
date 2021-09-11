package io.woong.filmpedia.network.service

import io.woong.filmpedia.data.movie.Genres
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface of TMDB movie genres.
 */
interface GenreService {

    @GET("genre/movie/list")
    fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null
    ): Call<Genres>
}