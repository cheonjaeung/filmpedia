package io.woong.filmpedia.repository

import io.woong.filmpedia.data.Movies
import io.woong.filmpedia.network.SearchService
import io.woong.filmpedia.network.TmdbClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchRepository {

    private val service: SearchService = TmdbClient.instance.create(SearchService::class.java)

    fun fetchMovies(
        key: String,
        lang: String,
        region: String,
        query: String,
        page: Int,
        onResponse: (results: Movies?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        val response = service.getMovies(
            apiKey = key,
            query = query,
            language = lang,
            page = page,
            region = region
        )

        if (response.isSuccessful) {
            onResponse(response.body()!!)
        } else {
            onResponse(null)
        }
    }
}
