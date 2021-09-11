package io.woong.filmpedia.network.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import io.woong.filmpedia.data.collection.Collection
import retrofit2.Call

interface CollectionService {

    @GET("collection/{collection_id}")
    fun getDetail(
        @Path("collection_id") collectionId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String? = null
    ): Call<Collection>
}
