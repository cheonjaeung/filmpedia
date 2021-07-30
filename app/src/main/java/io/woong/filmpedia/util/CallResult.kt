package io.woong.filmpedia.util

import io.woong.filmpedia.data.ErrorResponse
import io.woong.filmpedia.network.TmdbClient
import okhttp3.Headers
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

sealed class CallResult<T> {

    data class Success<T>(val response: Response<T>) : CallResult<T>() {
        val statusCode: Int = response.code()
        val headers: Headers = response.headers()
        val body: T = response.body() ?: throw Exception("No body exception")
        val raw: okhttp3.Response = response.raw()
    }

    data class Failure<T>(val response: Response<T>) : CallResult<T>() {
        val statusCode: Int = response.code()
        val headers: Headers = response.headers()
        val errorBody: ErrorResponse? = TmdbClient.instance.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java,
            ErrorResponse::class.java.annotations
        ).convert(response.errorBody())
        val raw: okhttp3.Response = response.raw()
    }

    data class Exception<T>(val call: Call<T>, val exception: Throwable) : CallResult<T>()
}
