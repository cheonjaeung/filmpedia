package io.woong.filmpedia.util

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun <T> Call<T>.request(
    onSuccess: (result: CallResult.Success<T>) -> Unit,
    onFailure: (result: CallResult.Failure<T>) -> Unit,
    onException: (result: CallResult.Exception<T>) -> Unit
) {
    this.enqueue(object : Callback<T> {
        override fun onResponse(call: Call<T>, response: Response<T>) {
            if (response.isSuccessful) {
                onSuccess(CallResult.Success(response))
            } else {
                onFailure(CallResult.Failure(response))
            }
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            onException(CallResult.Exception(call, t))
        }
    })
}
