package io.woong.filmpedia.network.service

import io.woong.filmpedia.data.people.MovieCredits
import io.woong.filmpedia.data.people.Person
import io.woong.filmpedia.data.people.Translations
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleService {

    @GET("person/{person_id}")
    fun getDetail(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
        @Query("append_to_response") appendToResponse: String? = null
    ): Call<Person>

    @GET("person/{person_id}/movie_credits")
    fun getMovieCredits(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
    ): Call<MovieCredits>

    @GET("person/{person_id}/images")
    fun getImages(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String
    ): Call<String>

    @GET("person/{person_id}/translations")
    fun getTranslations(
        @Path("person_id") personId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null,
    ): Call<Translations>
}
