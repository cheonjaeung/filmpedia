package io.woong.filmpedia.repository

import io.woong.filmpedia.data.movie.Movies
import io.woong.filmpedia.network.service.SearchService
import io.woong.filmpedia.network.TmdbClient
import io.woong.filmpedia.network.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SearchRepository {

    private val service: SearchService = TmdbClient.instance.create(SearchService::class.java)

    fun fetchMovies(
        key: String,
        lang: String,
        region: String,
        query: String,
        page: Int,
        onResponse: (result: Result<Movies>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        service.getMovies(
            apiKey = key,
            query = query,
            language = lang,
            page = page,
            region = region
        ).enqueue(object : Callback<Movies> {
            override fun onResponse(call: Call<Movies>, response: Response<Movies>) {
                if (response.isSuccessful) {
                    onResponse(Result.Success(response.body()))
                } else {
                    onResponse(Result.Failure(response.code(), response.errorBody()))
                }
            }

            override fun onFailure(call: Call<Movies>, t: Throwable) {
                if (t is IOException) {
                    onResponse(Result.NetworkError)
                }
            }
        })
    }
}
