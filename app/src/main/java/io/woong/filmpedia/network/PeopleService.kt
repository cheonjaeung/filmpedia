package io.woong.filmpedia.network

import io.woong.filmpedia.data.*
import io.woong.filmpedia.data.people.MovieCredits
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleService {

    @GET("person/{person_id}")
    suspend fun getDetail(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("append_to_response") appendToResponse: String? = null
    ): Response<Person>

    @GET("person/{person_id}/movie_credits")
    suspend fun getMovieCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
    ): Response<MovieCredits>

    @GET("person/{person_id}/external_ids")
    suspend fun getExternalIds(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
    ): Response<ExternalIds>

    @GET("person/{person_id}/images")
    suspend fun getImages(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String
    ): Response<String>

    @GET("person/{person_id}/translations")
    suspend fun getTranslations(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
    ): Response<PeopleTranslations>
}
