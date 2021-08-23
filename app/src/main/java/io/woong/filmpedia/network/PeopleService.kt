package io.woong.filmpedia.network

import io.woong.filmpedia.data.Person
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
}
