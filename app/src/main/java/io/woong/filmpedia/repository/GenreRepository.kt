package io.woong.filmpedia.repository

import io.woong.filmpedia.data.movie.Genres
import io.woong.filmpedia.network.service.GenreService
import io.woong.filmpedia.network.TmdbClient
import io.woong.filmpedia.network.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class GenreRepository {

    private val genreService: GenreService = TmdbClient.instance.create(GenreService::class.java)

    fun fetchGenres(
        key: String,
        lang: String,
        onResponse: (result: Result<Genres>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        genreService.getGenres(apiKey = key, language = lang)
            .enqueue(object : Callback<Genres> {
                override fun onResponse(call: Call<Genres>, response: Response<Genres>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Genres>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }
}
