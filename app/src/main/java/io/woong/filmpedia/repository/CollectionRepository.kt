package io.woong.filmpedia.repository

import io.woong.filmpedia.data.Collection
import io.woong.filmpedia.network.CollectionService
import io.woong.filmpedia.network.TmdbClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CollectionRepository {

    private val service: CollectionService = TmdbClient.instance.create(CollectionService::class.java)

    fun fetchDetail(
        key: String,
        id: Int,
        lang: String,
        onResponse: (series: Collection?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = service.getDetail(
            collectionId = id,
            apiKey = key,
            language = lang
        )

        if (response.isSuccessful) {
            onResponse(response.body())
        } else {
            onResponse(null)
        }
    }
}
