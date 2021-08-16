package io.woong.filmpedia.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import io.woong.filmpedia.data.Collection

interface CollectionService {

    @GET("collection/{collection_id}")
    suspend fun getDetail(
        @Path("collection_id") collectionId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null
    ): Response<Collection>
}
