package io.woong.filmpedia.repository

import io.woong.filmpedia.data.people.MovieCredits
import io.woong.filmpedia.data.people.Translations
import io.woong.filmpedia.data.people.Person
import io.woong.filmpedia.network.service.PeopleService
import io.woong.filmpedia.network.TmdbClient
import io.woong.filmpedia.network.data.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class PeopleRepository {

    private val service: PeopleService = TmdbClient.instance.create(PeopleService::class.java)

    fun fetchDetail(
        key: String,
        id: Int,
        lang: String,
        onResponse: (result: Result<Person>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        service.getDetail(personId = id, apiKey = key, language = lang)
            .enqueue(object : Callback<Person> {
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Person>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }

    fun fetchMovieCredits(
        key: String,
        id: Int,
        lang: String,
        onResponse: (result: Result<MovieCredits>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        service.getMovieCredits(personId = id, apiKey = key, language = lang)
            .enqueue(object : Callback<MovieCredits> {
                override fun onResponse(call: Call<MovieCredits>, response: Response<MovieCredits>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<MovieCredits>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }

    fun fetchTranslations(
        key: String,
        id: Int,
        lang: String,
        onResponse: (result: Result<Translations>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        service.getTranslations(personId = id, apiKey = key, language = lang)
            .enqueue(object : Callback<Translations> {
                override fun onResponse(call: Call<Translations>, response: Response<Translations>) {
                    if (response.isSuccessful) {
                        onResponse(Result.Success(response.body()))
                    } else {
                        onResponse(Result.Failure(response.code(), response.errorBody()))
                    }
                }

                override fun onFailure(call: Call<Translations>, t: Throwable) {
                    if (t is IOException) {
                        onResponse(Result.NetworkError)
                    }
                }
            })
    }
}
