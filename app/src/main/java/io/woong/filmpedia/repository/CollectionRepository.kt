package io.woong.filmpedia.repository

import io.woong.filmpedia.data.collection.Collection
import io.woong.filmpedia.network.service.CollectionService
import io.woong.filmpedia.network.TmdbClient
import io.woong.filmpedia.network.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class CollectionRepository {

    private val service: CollectionService = TmdbClient.instance.create(CollectionService::class.java)

    fun fetchDetail(
        key: String,
        id: Int,
        lang: String,
        onResponse: (result: Result<Collection>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        service.getDetail(collectionId = id, apiKey = key, language = lang)
            .enqueue(object : Callback<Collection> {
                override fun onResponse(call: Call<Collection>, response: Response<Collection>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Collection>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }
}
